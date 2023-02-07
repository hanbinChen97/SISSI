package de.hsh.sissi_kneipen.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import de.hsh.sissi_kneipen.R
import de.hsh.sissi_kneipen.viewmodel.RatingViewModel
import de.hsh.sissi_kneipen.viewmodel.RatingsViewModel
import de.hsh.sissi_kneipen.viewmodel.Rec

class RatingsAdapter (
    context: Context,
    private val dataSource:  ArrayList<RatingViewModel>): BaseAdapter(){

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }


    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.item_rating, parent, false)

        val name: TextView = rowView.findViewById(R.id.rating_item_name)
        val category: TextView = rowView.findViewById(R.id.rating_item_category)
        val ratingBar: RatingBar = rowView.findViewById(R.id.rating_item_ratingBar)

        val ratingItem: RatingViewModel = getItem(position) as RatingViewModel
        val ratingVal = ratingItem.rating + 2.5

        name.text = ratingItem.name
        category.text = ratingItem.category
        ratingBar.rating = ratingVal.toFloat()

        return rowView
    }
}