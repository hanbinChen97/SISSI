package de.hsh.sissi_kneipen.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import de.hsh.sissi_kneipen.R
import de.hsh.sissi_kneipen.databinding.MapFragmentBinding
import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.model.GpsPosition
import de.hsh.sissi_kneipen.ui.mainUi.GpsProcessor
import de.hsh.sissi_kneipen.util.DirectionsParser
import de.hsh.sissi_kneipen.util.NOT_IN_BAR
import de.hsh.sissi_kneipen.viewmodel.MapViewModel
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.math.abs


class MapFragment : Fragment(), GpsProcessor,
    OnMapReadyCallback{

    private lateinit var mMap:GoogleMap
    private val markers: Queue<Marker> = LinkedList<Marker>()
    private var bars: HashMap<String, Bar> = HashMap<String, Bar>()
    private val GOOGLE_API_KEY: String = "AIzaSyAY6xqNhn4wXGzBL45eWiA3Fwlx0tnfdLk"
    private var index : Int = 1
    private val EARTHRADIUS : Int = 6366198


    companion object {
        fun newInstance() = MapFragment()
    }

    lateinit var mBinding: MapFragmentBinding
    lateinit var viewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.map_fragment, container, false)

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)

        mBinding.mapVm = viewModel
        mBinding.lifecycleOwner = this
    }

    //TODO(what is this)
    private fun runtime_permissions(): Boolean {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                this.activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this.activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 100
            )
            return true
        }
        return false
    }

    //TODO(what is this)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            } else {
                runtime_permissions()
            }
        }
    }

    override fun onMapReady(p0: GoogleMap?) {

    }

    override fun update(
        gpsPosition: GpsPosition,
        target: Bar,
        autoAdjust: Boolean
    ) {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.google_map) as SupportMapFragment?

        //TODO(comment)
        mapFragment!!.getMapAsync { googleMap ->

            googleMap!!.uiSettings.isZoomControlsEnabled = true
            googleMap!!.isMyLocationEnabled = true

            googleMap.clear()


            //Initialization for Bar marker
            var barsMarker = setCustomMark(R.drawable.ic_local_bar_35dp)
            //Initialization for user marker
            var userMarker = setCustomMark(R.drawable.ic_person_pin_circle_35dp)

            val position = LatLng(gpsPosition.latitude, gpsPosition.longitude)
            var marker = googleMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(gpsPosition.latitude.toString() + ", " + gpsPosition.longitude.toString())
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                            userMarker?.toBitmap(
                                userMarker.intrinsicWidth,
                                userMarker.intrinsicHeight,
                                null
                            )
                        )
                    )
            )

            markers.add(marker)
            if (markers.size > 1) {
                markers.element().remove()     //marker.remove() --> clean the last marker position
                markers.poll()                 //poll marker from markers
            }

            val myActivity = activity as SISSIActivity
            bars = myActivity.repository.sissiDAO.getBars()
            //mark bars on map
            bars.forEach {
                var barName = it.value.name
                var barPosition = it.value.position
                var LatLng = LatLng(barPosition.latitude, barPosition.longitude)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng)
                        .title(barName)
                        .snippet("category: " + it.value.category)
                        .icon(
                            BitmapDescriptorFactory.fromBitmap(
                                barsMarker?.toBitmap(
                                    barsMarker.intrinsicWidth,
                                    barsMarker.intrinsicHeight,
                                    null
                                )
                            )
                        )
                )
            }

            if(index == 1){
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15.3f))
                index += 1
                Log.i("startZoom", "startZoom")
            }


            if (target != Bar(NOT_IN_BAR)) {
                class TaskParser :
                    AsyncTask<String?, Void?, List<List<HashMap<String, String>>>?>() {
                    override fun doInBackground(vararg strings: String?): List<List<HashMap<String, String>>>? {
                        var jsonObject: JSONObject? = null
                        var routes: List<List<HashMap<String, String>>>? =
                            null
                        try {
                            jsonObject = JSONObject(strings[0])
                            val directionsParser = DirectionsParser()
                            routes = directionsParser.parse(jsonObject)
                            Log.d("direction", routes.toString())
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        return routes
                    }

                    override fun onPostExecute(lists: List<List<HashMap<String, String>>>?) { //Get list route and display it into the map
                        var points: ArrayList<LatLng>? = null
                        var polylineOptions: PolylineOptions? = null
                        for (path in lists!!) {
                            points = ArrayList()
                            polylineOptions = PolylineOptions()
                            for (point in path) {
                                val lat = point["lat"]!!.toDouble()
                                val lon = point["lon"]!!.toDouble()
                                points.add(LatLng(lat, lon))
                            }
                            polylineOptions.addAll(points)
                            polylineOptions.width(15f)
                            polylineOptions.color(Color.rgb(255, 0, 0))
                            polylineOptions.geodesic(true)
                        }
                        if (polylineOptions != null) {
                            googleMap.addPolyline(polylineOptions)
                        } else {
//                            Toast.makeText(
//                                activity?.applicationContext,
//                                "",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    }
                }

                class TaskRequestDirections :
                    AsyncTask<String?, Void?, String>() {
                    override fun doInBackground(vararg strings: String?): String {
                        var responseString = ""
                        try {
                            responseString = requestDirection(strings[0])
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        return responseString
                    }

                    override fun onPostExecute(s: String) {
                        super.onPostExecute(s)
                        //Parse json here
                        val taskParser = TaskParser()
                        taskParser.execute(s)
                    }
                }

                val targetLatLng = LatLng(target.position.latitude, target.position.longitude)
                val url = getRequestUrl(position, targetLatLng)
                val taskRequestDirections = TaskRequestDirections()
                taskRequestDirections.execute(url)


                if (autoAdjust == true) {
                    val width = resources.displayMetrics.widthPixels
                    val height = resources.displayMetrics.heightPixels
                    var bounds = createBoundsWithMinDiagonal(targetLatLng, position)
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            width,
                            height,
                            10
                        )
                    )
                    Log.i("bounds", "bounds")
                }
            }

        }
    }

    private fun createBoundsWithMinDiagonal(firstPosition: LatLng, secondPosition: LatLng): LatLngBounds? {
        var builder = LatLngBounds.Builder()
        builder.include(firstPosition)
        builder.include(secondPosition)
        var latitudeDiff = abs(firstPosition.latitude - secondPosition.latitude)
        var longitudeDiff = abs(firstPosition.longitude - secondPosition.longitude)
        var bounds = builder.build()
        var center = bounds.center
        var northEast = move(center, latitudeDiff * 1250 / 0.01, longitudeDiff * 920 / 0.01)
        var southWest = move(center, -(latitudeDiff * 1250 / 0.01), -(longitudeDiff * 920 / 0.01))
        builder.include(southWest)
        builder.include(northEast)
        return builder.build()
    }

    private fun move(startLL: LatLng, toNorth: Double, toEast:Double): LatLng{
        var lonDiff = meterToLongitude(toEast, startLL.latitude)
        var latDiff = meterToLatitude(toNorth)
        return LatLng(startLL.latitude + latDiff, startLL.longitude + lonDiff)
    }

    private fun meterToLongitude(meterToEast: Double, latitude: Double): Double{
        var latArc = Math.toRadians(latitude)
        var radius = Math.cos(latArc) * EARTHRADIUS
        var rad = meterToEast/radius
        return Math.toDegrees(rad)
    }

    private fun meterToLatitude(meterToNorth: Double): Double{
        var rad = meterToNorth/EARTHRADIUS
        return Math.toDegrees(rad)
    }


    private fun setCustomMark(resouceId: Int): Drawable? {

        var customMarker = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resources.getDrawable(resouceId,null)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }

        return customMarker
    }



    private fun getRequestUrl(origin: LatLng, dest: LatLng): String {
        //Value of origin
        val str_org = "origin=" + origin.latitude + "," + origin.longitude
        //Value of destination
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        //Set value enable the sensor
        val sensor = "sensor=false"
        //Mode for find direction
        val mode = "mode=walking"
        //Build the full param
        val param = "$str_org&$str_dest&$sensor&$mode&key=$GOOGLE_API_KEY"
        //Output format
        val output = "json"
        //Create url to request
        return "https://maps.googleapis.com/maps/api/directions/$output?$param"
    }

    @Throws(IOException::class)
    private fun requestDirection(reqUrl: String?): String {
        var responseString = ""
        var inputStream: InputStream? = null
        var httpURLConnection: HttpURLConnection? = null
        try {
            val url = URL(reqUrl)
            httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.connect()
            //Get the response result
            inputStream = httpURLConnection.inputStream
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuffer = StringBuffer()
            var line: String? = ""
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuffer.append(line)
            }
            responseString = stringBuffer.toString()
            bufferedReader.close()
            inputStreamReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                inputStream.close()
            }
            httpURLConnection!!.disconnect()
        }
        return responseString
    }

}
