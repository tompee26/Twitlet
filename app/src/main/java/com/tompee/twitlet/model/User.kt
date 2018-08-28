package com.tompee.twitlet.model

import java.util.*

data class User(var email: String = "",
                var isAuthenticated: Boolean = false,
                var nickname: String = "",
                var image: ByteArray = ByteArray(0)) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (email != other.email) return false
        if (isAuthenticated != other.isAuthenticated) return false
        if (nickname != other.nickname) return false
        if (!Arrays.equals(image, other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + isAuthenticated.hashCode()
        result = 31 * result + nickname.hashCode()
        result = 31 * result + Arrays.hashCode(image)
        return result
    }
}