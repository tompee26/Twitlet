package com.tompee.twitlet.model

import android.graphics.Bitmap
import java.util.*

data class User(var email: String = "",
                var isAuthenticated: Boolean = false,
                var nickname: String = "",
                var imageByteArray: ByteArray = ByteArray(0)) {

    var bitmap: Bitmap? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (email != other.email) return false
        if (isAuthenticated != other.isAuthenticated) return false
        if (nickname != other.nickname) return false
        if (!Arrays.equals(imageByteArray, other.imageByteArray)) return false
        if (bitmap != other.bitmap) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + isAuthenticated.hashCode()
        result = 31 * result + nickname.hashCode()
        result = 31 * result + Arrays.hashCode(imageByteArray)
        result = 31 * result + (bitmap?.hashCode() ?: 0)
        return result
    }
}