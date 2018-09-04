package com.tompee.twitlet.core.image.picasso

import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.tompee.twitlet.core.image.Renderer
import java.io.File

class PicassoRenderer : Renderer {

    private val instance: Picasso = Picasso.get()

    init {
        instance.setIndicatorsEnabled(true)
    }

    override fun displayImage(url: String, view: ImageView) {
        instance.load(url).into(view)
    }

    override fun displayImage(file: File, view: ImageView) {
        instance.load(file).into(view)
    }
}