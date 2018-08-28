package com.tompee.twitlet.core.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

class ImageProcessor(private val context: Context) {

    fun getBitmapFromUri(uri: Uri): Bitmap =
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

    fun getByteStreamFromBitmap(bitmap: Bitmap): ByteArray {
        val bao = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao)
        bitmap.recycle()
        return bao.toByteArray()
    }
}