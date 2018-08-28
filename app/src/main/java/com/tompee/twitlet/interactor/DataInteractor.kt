package com.tompee.twitlet.interactor

import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.database.UserEntity
import com.tompee.twitlet.core.image.ImageProcessor
import com.tompee.twitlet.model.User
import io.reactivex.Completable
import io.reactivex.Single

class DataInteractor(private val userDao: UserDao,
                     private val imageProcessor: ImageProcessor) {

    fun getUser(email: String): Single<User> {
        return userDao.getUser(email)
                .map {
                    User(email = it.email, nickname = it.nickname,
                            image = Base64.decode(it.image, Base64.DEFAULT))
                }
    }

    fun saveUser(user: User): Completable {
        return userDao.saveUser(UserEntity(email = user.email,
                nickname = user.nickname, image = Base64.encodeToString(user.image, Base64.DEFAULT)))
    }

    fun getBitmapFromUri(uri: Uri): Bitmap = imageProcessor.getBitmapFromUri(uri)

    fun getByteArrayFromBitmap(bitmap: Bitmap) = imageProcessor.getByteStreamFromBitmap(bitmap)
}