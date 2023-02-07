package de.hsh.sissi_kneipen.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.*
import de.hsh.sissi_kneipen.R
import de.hsh.sissi_kneipen.manager.Repository
import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.model.GpsPosition
import de.hsh.sissi_kneipen.ui.mainUi.InfoFragment
import de.hsh.sissi_kneipen.ui.mainUi.RatingFragment
import de.hsh.sissi_kneipen.ui.mainUi.RecFragment
import de.hsh.sissi_kneipen.ui.mainUi.TabsFragment
import de.hsh.sissi_kneipen.util.*
import kotlinx.android.synthetic.main.info_fragment.*
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.android.synthetic.main.rating_fragment.*
import kotlinx.android.synthetic.main.rec_fragment.*
import kotlinx.android.synthetic.main.tab_fragment.*
import kotlin.system.exitProcess


/**
 * callback function of GPS service missed
 */
class SISSIActivity : AppCompatActivity(),
    StartFragment.OnFragmentInteractionListener {

    private var currentFragment: Fragment = StartFragment.newInstance()
    val tabsFragment = TabsFragment.newInstance()
    val gpsFragment = GpsFragment.newInstance()
    val recFragment = RecFragment.newInstance()
    val infoFragment = InfoFragment.newInstance()
    val mapFragment = MapFragment.newInstance()
    val ratingFragment = RatingFragment.newInstance()

    val repository = Repository()
    private var firstGps = true
    var pubNow = Bar(NOT_IN_BAR)
    var gpsPosition = GpsPosition(0.0, 0.0)

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var requestingLocationUpdates = false

    var state = InfoStateEnum.NOTHING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        loadFragment(savedInstanceState)
        requestLocationPermission()

        if (checkInternet()) {
            requestingLocationUpdates = true

            //there is network request
            repository.init(this)

            initGps()
        } else {
            showNoInternetDialog()
        }
    }

    private fun checkInternet() =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetwork != null

    fun showNoInternetDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Fehlermeldung")
            .setMessage(ERROR_MESSAGE)
            .setPositiveButton("OK") { _, _ ->
                finish()
                exitProcess(0)
            }.show()
    }

    private fun initGps() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                gpsPosition.latitude = p0!!.lastLocation.latitude
                gpsPosition.longitude = p0.lastLocation.longitude

                checkPosition(gpsPosition)
                mapFragment.update(gpsPosition, repository.target, false)
                gpsFragment.update(gpsPosition, repository.target, false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) {
            startLocationUpdates()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        repository.checkPath(this)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.create()?.apply {
                interval = REFRESHING_INTERVAL
                fastestInterval = REFRESHING_INTERVAL / 2
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            },
            locationCallback,
            null
        )
    }

    private fun requestLocationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                100
            )
            return true
        }
        return false
    }

    private fun loadFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, currentFragment)
                .add(R.id.container, tabsFragment, "gpsFragment").hide(tabsFragment)
                .add(R.id.container, recFragment, "recFragment").hide(recFragment)
                .add(R.id.container, infoFragment, "infoFragment").hide(infoFragment)
                .add(R.id.container, mapFragment, "mapFragment").hide(mapFragment)
                .add(R.id.container, ratingFragment, "ratingFragment").hide(ratingFragment)
                .add(R.id.container, gpsFragment, "gpsFragment").hide(gpsFragment)
                .commit()
        }
    }

    private fun showRec(){
        val recommendations = recFragment.viewModel.recommendations
        val recsAdapter =
            RecsAdapter(this, recommendations)
        recList.adapter = recsAdapter
    }

    private fun showRating(){
        val ratings = ratingFragment.viewModel.ratingList
        ratings.sortBy { it.rating }
        ratings.reverse()
        val ratingsAdapter = RatingsAdapter(this, ratings)
        ratingList.adapter = ratingsAdapter
    }

    fun switchFragment(f: Fragment) {
        val fragmentTransaction: FragmentTransaction= supportFragmentManager.beginTransaction()

        if (currentFragment != f) {
            fragmentTransaction.show(tabsFragment)
            fragmentTransaction.hide(gpsFragment)
            if (!f.isAdded) {
                fragmentTransaction.hide(currentFragment).add(R.id.container, f)
            } else {
                fragmentTransaction.hide(currentFragment).show(f)
            }
            //show gpsFragment on bottom
            fragmentTransaction.show(gpsFragment)
        }

        fragmentTransaction.commit()
        currentFragment = f

    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // first time: myCurrentBar = NOT_IN_BAR
    private fun firstGps(pubNow: Bar){
        if (firstGps) {
            while (repository.doingJob) {
                SystemClock.sleep(500)
                Log.i(SISSI, "wait bar hash map loading")
            }
            ratingFragment.viewModel.update(repository.ratings, repository.sissiDAO)
            showRating()

            repository.doingJob = true
            repository.sendRating()
            while (repository.doingJob) {
                SystemClock.sleep(500)
                Log.i(SISSI, "wait send-rating reply")
            }

            repository.doingJob = true
            repository.getRecommendations()

            while (repository.doingJob) {
                SystemClock.sleep(500)
                Log.i(SISSI, "wait recommendations reply")
            }

            recFragment.update(repository.recs)
            showRec()

            toRecOrInfo(pubNow)

            firstGps = false
        }
    }

    private fun toRecOrInfo(pubNow: Bar){
        if (!isNowInPub(pubNow)) {  // to rec
            tabsFragment.state.toRec(this)
            tabsFragment.state = TabStateEnum.REC
            tabsFragment.refreshTabs()
            tab3.isClickable = false
            tab3.setBackgroundResource(R.color.colorDisable)
        } else {
            chronometer.base = SystemClock.elapsedRealtime()
            goLayout.visibility = View.GONE
            timeLayout.visibility = View.VISIBLE
            repository.addArrivalAndSavePath(pubNow, this)
            infoFragment.viewModel.update(pubNow)
            repository.myCurrentBar = pubNow
            tabsFragment.state.toInfo(this)
            tabsFragment.state = TabStateEnum.INFO
            tabsFragment.refreshTabs()
            chronometer.start()
        }
    }

    private fun leavePub(pubNow: Bar){
        if (wasInPub(repository.myCurrentBar) && !isNowInPub(pubNow)) {
            Log.i(SISSI, "leave ${repository.myCurrentBar}")
            repository.myCurrentBar = Bar(NOT_IN_BAR)
            state = InfoStateEnum.BUTTON
            state.infoState(this)
            repository.addArrivalAndSavePath(pubNow, this)
            ratingFragment.viewModel.update(repository.ratings, repository.sissiDAO)
            showRating()

            repository.doingJob = true
            repository.sendRating()
            repository.getRecommendations()

            while (repository.doingJob) {
                Log.i(SISSI, "wait recommendations reply")
            }
            Handler().postDelayed({
                recFragment.update(repository.recs)
                showRec()
            }, 700)

            chronometer.stop()
            tabsFragment.state.toRec(this)
            tabsFragment.state = TabStateEnum.REC
            tabsFragment.refreshTabs()
            tab3.isClickable = false
            tab3.setBackgroundResource(R.color.colorDisable)
        }
    }

    private fun enterPub(pubNow: Bar){
        if (!wasInPub(repository.myCurrentBar) && isNowInPub(pubNow)) {
            if (pubNow == repository.target || mapFragment.destination.text == DES_PREFIX) {
                Log.i(SISSI, "entry ${repository.target}")
                chronometer.base = SystemClock.elapsedRealtime()
                state = InfoStateEnum.SOJOURN
                state.infoState(this)
                mapFragment.destination.text = DES_PREFIX
                //no more target but current bar
                repository.target = Bar(NOT_IN_BAR)
                repository.myCurrentBar = pubNow
                repository.addArrivalAndSavePath(pubNow, this)
                infoFragment.viewModel.update(pubNow)
                tabsFragment.state.toInfo(this)
                tabsFragment.state = TabStateEnum.INFO
                tabsFragment.refreshTabs()
                chronometer.start()
            }
        }
    }

    private fun checkPosition(gpsPosition: GpsPosition) {
        pubNow = repository.inWhichBar(gpsPosition)
        firstGps(pubNow)
        leavePub(pubNow)
        enterPub(pubNow)
    }

    private fun isNowInPub(pubNow: Bar): Boolean {
        return pubNow.id != NOT_IN_BAR
    }

    private fun wasInPub(myCurrentBar: Bar): Boolean {
        return myCurrentBar.id != NOT_IN_BAR
    }

}
