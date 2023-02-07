package de.hsh.sissi_kneipen.persistence

import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.model.Rating
import de.hsh.sissi_kneipen.model.Recommendation
import java.util.*

class SissiLocalDAO : SissiDAO {
    private var bars = BarsLocalInitializer().loadBars()

    override fun setUseId(userId: String) {
    }

    override fun setBars(bars: HashMap<String, Bar>) {
    }

    override fun getBars(): HashMap<String, Bar> {
        return  bars
    }

    override fun getBar(id: String): Bar {
        return bars[id]!!
    }

    override fun sendRating(ratings: ArrayList<Rating>) {
    }

    override fun getRecommendations(): ArrayList<Recommendation> {
        return arrayListOf(
            Recommendation(1, getBar("7")),
            Recommendation(2, getBar("6")),
            Recommendation(3, getBar("10"))
        )
    }
}