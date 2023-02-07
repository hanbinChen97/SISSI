package de.hsh.sissi_kneipen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import de.hsh.sissi_kneipen.persistence.SissiDAO
import de.hsh.sissi_kneipen.util.SISSI
import java.util.*

class RatingsViewModel : ViewModel() {
    // 0..n
    private val ratingHashMap = HashMap<String, RatingViewModel>()
    lateinit var ratingList: ArrayList<RatingViewModel>
    fun update(ratings: HashMap<String, Double>, sissiDAO: SissiDAO) {
        for (rating in ratings){
            val id = rating.key
            val ratingViewModel = RatingViewModel(sissiDAO.getBar(id).name, sissiDAO.getBar(id).category, rating.value)
            ratingHashMap[id] = ratingViewModel
        }
        ratingList = ArrayList(ratingHashMap.size)
        for (item in ratingHashMap){
            ratingList.add(RatingViewModel(item.value.name, item.value.category, item.value.rating))
        }
        Log.i(SISSI, "ratingfragment updated: $ratingList")
    }
}

class RatingViewModel(val name: String, val category: String, val rating: Double) : ViewModel() {
    override fun toString(): String {
        return "$name, $rating"
    }
}



