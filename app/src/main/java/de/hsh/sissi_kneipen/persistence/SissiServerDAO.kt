package de.hsh.sissi_kneipen.persistence

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.model.Rating
import de.hsh.sissi_kneipen.model.Recommendation
import de.hsh.sissi_kneipen.util.PUB_PATH
import de.hsh.sissi_kneipen.util.SISSI
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class SissiServerDAO : SissiDAO {
    private var bars: HashMap<String, Bar> = HashMap()
    private val json = Json(JsonConfiguration.Stable)
    private var userId = ""

    override fun setUseId(userId: String){
        this.userId = userId
    }

    override fun setBars(bars: HashMap<String, Bar>) {
        this.bars = bars
    }

    override fun getBars(): HashMap<String, Bar> {
        return  bars
    }

    override fun getBar(id: String): Bar {
        return bars[id]!!
    }

    // size = 5 (from Server)
    override fun getRecommendations(): ArrayList<Recommendation> {
        val result = Fuel.post("$PUB_PATH/recommendations")
            .jsonBody(
                json.stringify(
                    UserIDJson.serializer(),
                    UserIDJson(userId)
                )
            ).response()
        val rec = String(result.second.data)
        val recWithPoiIdJson = json.parse(ArrayListSerializer(RecWithPoiIdJson.serializer()), rec)
        val recList: ArrayList<Recommendation> = ArrayList()
        Log.i(SISSI, "get rec: $rec")

        // size+1 excluded
        for (i in 1 until (recWithPoiIdJson.size+1)){
            val recommendation = Recommendation(i, getBar(recWithPoiIdJson[i-1].poi_id))
            Log.i(SISSI, recWithPoiIdJson[i - 1].toString())
            recList.add(recommendation)
        }

        Log.i(SISSI, "get rec: $recList")

        return recList
    }

    override fun sendRating(ratings: ArrayList<Rating>) {
        val ratingJson = ArrayList<RatingJson>()
        for (rating in ratings){
            ratingJson.add(RatingJson(rating.bar.id, rating.rating))
        }
        val date: String = json.stringify(RatingsJson.serializer(), RatingsJson(userId, ratingJson))
        val result = Fuel.post("$PUB_PATH/rating")
            .jsonBody(date).response()
        Log.i(SISSI, " post rating: $result")
    }
}