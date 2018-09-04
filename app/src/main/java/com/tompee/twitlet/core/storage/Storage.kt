package com.tompee.twitlet.core.storage

import android.net.Uri
import io.reactivex.Completable
import io.reactivex.Single

interface Storage {
    fun uploadProfileImage(email: String, uri: Uri): Single<String>

    fun uploadPostImage(path: String, email: String, postId: String): Single<String>
    fun deletePostImage(postImage: String): Completable
}