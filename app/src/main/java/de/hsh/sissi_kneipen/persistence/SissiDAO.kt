package de.hsh.sissi_kneipen.persistence

import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.model.Rating
import de.hsh.sissi_kneipen.model.Recommendation
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class RecWithPoiIdJson(val recommendation: Int, val poi_id: String)

@Serializable
data class UserIDJson(val user_id: String)

@Serializable
data class RatingJson(val poi_id: String, val z_score: Double)

@Serializable
data class RatingsJson(val user_id: String, val z_scores: List<RatingJson>)

interface SissiDAO {
    fun setUseId(userId: String)
    fun setBars(bars: HashMap<String, Bar>)
    fun getBars(): HashMap<String, Bar>
    fun getBar(id: String): Bar
    fun sendRating(ratings: ArrayList<Rating>)
    fun getRecommendations(): ArrayList<Recommendation>
}