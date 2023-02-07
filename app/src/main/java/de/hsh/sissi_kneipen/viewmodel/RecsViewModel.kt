package de.hsh.sissi_kneipen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import de.hsh.sissi_kneipen.model.Recommendation
import de.hsh.sissi_kneipen.util.PICTURE_PATH

class RecsViewModel : ViewModel() {
    var recommendations = arrayListOf<Rec>(
        Rec(),
        Rec(),
        Rec(),
        Rec(),
        Rec()
    )

    fun update(recs: ArrayList<Recommendation>) {
        val size = recs.size   // default size is 5
        if (size < 5){
            // for example: size=3, remove Rec4 and Rec5, index are 3 and 4
            for (i in size until 4){
                recommendations.removeAt(i)
            }
        }
        //rec.rating 1-5
        for (rec in recs){
            recommendations[rec.rating-1].id = rec.bar.id
            recommendations[rec.rating-1].name = rec.bar.name
            recommendations[rec.rating-1].category = rec.bar.category
            recommendations[rec.rating-1].imageUrl = rec.bar.thumbnail_link

            Log.i("result","name "+rec.rating+": "+recommendations[rec.rating-1].name)
        }
    }

}

class Rec(
    var id: String,   //poi_id
    var name: String,
    var category: String,
    var imageUrl: String
) : ViewModel() {
    constructor(): this(
        "",
        "",
        "",
        "${PICTURE_PATH}1.jpg"
    )
}