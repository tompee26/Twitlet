package com.tompee.twitlet.model

import android.graphics.Bitmap

class User(var email: String = "",
           var isAuthenticated: Boolean = false,
           var nickname: String = "",
           var imageByteArray: ByteArray = ByteArray(0)) {

    var bitmap: Bitmap? = null
}