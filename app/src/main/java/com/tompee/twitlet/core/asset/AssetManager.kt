package com.tompee.twitlet.core.asset

import android.content.Context
import android.os.Build
import android.text.Html
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class AssetManager(private val context: Context) {

    fun getStringFromAsset(filename: String): String {
        val buffer = StringBuilder()
        val inputStream: InputStream
        try {
            inputStream = context.assets.open(filename)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var str = bufferedReader.readLine()
            while (str != null) {
                buffer.append(str)
                str = bufferedReader.readLine()
            }
            bufferedReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return buffer.toString()
    }

}