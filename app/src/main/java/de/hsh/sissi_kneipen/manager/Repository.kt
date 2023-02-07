package de.hsh.sissi_kneipen.manager

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import de.hsh.sissi_kneipen.model.*
import de.hsh.sissi_kneipen.persistence.BarsServerInitializer
import de.hsh.sissi_kneipen.persistence.SissiDAO
import de.hsh.sissi_kneipen.persistence.SissiServerDAO
import de.hsh.sissi_kneipen.ui.SISSIActivity
import de.hsh.sissi_kneipen.util.NOT_IN_BAR
import de.hsh.sissi_kneipen.util.RADIUS
import de.hsh.sissi_kneipen.util.SISSI

class Repository {
    //config loading Data local or from server
    val sissiDAO: SissiDAO = SissiServerDAO()

    lateinit var userId: String
    lateinit var path: ArrayList<Arrival>
    lateinit var ratings: HashMap<String, Double>
    //lateinit var recs: ArrayList<Recommendation>
    var recs = ArrayList<Recommendation>()
    var myCurrentBar = Bar(NOT_IN_BAR)
    var target = Bar(NOT_IN_BAR)

    var doingJob = false

    private val ratingManager = RatingManager()
    private val pathManager = PathManager()

    fun init(context: Context) {
        userId = UserIdManager().getId(context)
        Log.i(SISSI, "userId: $userId")
        sissiDAO.setUseId(userId)

        doingJob = true
        Log.i(SISSI, "bar hash map task begins and running status: $doingJob")
        BarsHashMapTask().execute()

        path = pathManager.getPath(context)
        Log.i(SISSI, "init read path: $path")
        checkPath(context)

        ratings = ratingManager.getRating(path)
        Log.i(SISSI, "rating: $ratings")
    }

    fun checkPath(context: Context) {
        if (path.isNotEmpty() && path.last().bar.id != NOT_IN_BAR) {
            Log.i(SISSI, "fixed path.")
            addArrivalAndSavePath(Bar(NOT_IN_BAR), context)
        }
    }

    fun addArrivalAndSavePath(bar: Bar, context: Context) {
        path.add(Arrival(bar, System.currentTimeMillis()))
        Log.i(SISSI, "write path: $path")

        pathManager.savePath(context, path)
        ratings = ratingManager.getRating(path)
        Log.i(SISSI, "updated rating: $ratings")

    }

    /**
     * @return in which bar, else -1
     */
    fun inWhichBar(p: GpsPosition): Bar {

        sissiDAO.getBars().forEach {
            if (isInPub(it.value, p)) {
                return it.value
            }
        }

        return Bar(NOT_IN_BAR)
    }

    private fun isInPub(bar: Bar, p: GpsPosition): Boolean {
        return (((bar.position.latitude - p.latitude) * (bar.position.latitude - p.latitude)
                + (bar.position.longitude - p.longitude) * (bar.position.longitude - p.longitude))
                < RADIUS * RADIUS)
    }

    fun getRecommendations() {
        GetRecommendationsTask().execute()
    }

    fun sendRating() {
        SendRatingTask().execute()
    }

    inner class GetRecommendationsTask :
        AsyncTask<Void, Void, ArrayList<Recommendation>>() {
        override fun doInBackground(vararg params: Void?): ArrayList<Recommendation> {
            recs = sissiDAO.getRecommendations()
            doingJob = false
            Log.i(SISSI, recs.size.toString() +  " Recommendations")
            return recs
        }

        override fun onPostExecute(result: ArrayList<Recommendation>?) {
            super.onPostExecute(result)
            if (result!!.size == 0) {
                SISSIActivity().showNoInternetDialog()
            }
        }

    }

    inner class BarsHashMapTask :
        AsyncTask<Void, Void, HashMap<String, Bar>>() {
        override fun doInBackground(vararg params: Void?): HashMap<String, Bar> {
            return BarsServerInitializer().loadBars()   //more than 10 seconds, about 12
        }

        override fun onPostExecute(result: HashMap<String, Bar>?) {
            super.onPostExecute(result)
            Log.i(SISSI, result!!.size.toString() + " Bars")
            sissiDAO.setBars(result)
            doingJob = false
            Log.i(SISSI, "bar hash map task running status: $doingJob")
        }
    }

    inner class SendRatingTask :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            doingJob = true

            sissiDAO.sendRating(hashMapToArrayList(ratings))
            doingJob = false

            return null
        }
    }


    private fun hashMapToArrayList(hashMap: HashMap<String, Double>): ArrayList<Rating> {
        val ratings = ArrayList<Rating>()

        for (rating in hashMap) {
            ratings.add(Rating(sissiDAO.getBar(rating.key), rating.value))
        }

        return ratings
    }
}
