package de.hsh.sissi_kneipen.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class MyBindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("myImageUrl")
        fun ImageView.loadImage(url: String) {
            Glide.with(context).load(url).into(this)
        }
    }
}