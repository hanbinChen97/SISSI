package de.hsh.sissi_kneipen.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import de.hsh.sissi_kneipen.R
import de.hsh.sissi_kneipen.viewmodel.Rec
import java.net.URL


class RecsAdapter(
    context: Context,
    private val dataSource:  ArrayList<Rec>): BaseAdapter(){

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

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.item_rec, parent, false)

        val img: ImageView = rowView.findViewById(R.id.iv)
        val name: TextView = rowView.findViewById(R.id.name)
        val category: TextView = rowView.findViewById(R.id.category)

        val recommendation: Rec = getItem(position) as Rec

        name.text = recommendation.name
        category.text = recommendation.category

        DownloadImageTask(img).execute(recommendation.imageUrl)

        return rowView

    }

    class DownloadImageTask(bmImage: ImageView) :
        AsyncTask<String, Void, Bitmap?>() {
        var bmImage: ImageView? = bmImage
        override fun doInBackground(vararg params: String): Bitmap? {
            val urlDisplay = params[0]
            var bmp: Bitmap? = null
            try {
                val inputStream = URL(urlDisplay).openStream()
                bmp = BitmapFactory.decodeStream(inputStream)
            }catch (e: Exception){
                e.printStackTrace()
            }
            return bmp
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            bmImage?.setImageBitmap(result)
        }
    }
}