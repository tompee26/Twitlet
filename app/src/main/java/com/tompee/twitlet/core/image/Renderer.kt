package com.tompee.twitlet.core.image

import android.widget.ImageView
import java.io.File

interface Renderer {
    fun displayImage(url: String, view: ImageView)
    fun displayImage(file: File, view: ImageView)
}