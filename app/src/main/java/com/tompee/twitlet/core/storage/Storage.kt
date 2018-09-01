package com.tompee.twitlet.core.storage

import android.net.Uri
import io.reactivex.Single

interface Storage {
    fun uploadProfileImage(email: String, uri: Uri): Single<String>
}