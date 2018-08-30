package com.tompee.twitlet.interactor

import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import com.tompee.twitlet.base.BaseInteractor
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.database.UserEntity
import com.tompee.twitlet.core.image.ImageProcessor
import com.tompee.twitlet.model.User
import io.reactivex.Completable

class ProfileInteractor(private val imageProcessor: ImageProcessor,
                        private val userDao: UserDao,
                        private val loggedInUser: User) : BaseInteractor {

    fun getBitmapFromUri(uri: Uri): Bitmap = imageProcessor.getBitmapFromUri(uri)

    fun getByteArrayFromBitmap(bitmap: Bitmap) = imageProcessor.getByteStreamFromBitmap(bitmap)

    fun saveUser(user: User): Completable {
        return userDao.saveUser(UserEntity(user.email, user.nickname,
                Base64.encodeToString(user.imageByteArray, Base64.DEFAULT)))
    }

    fun updateLoggedInUser(name: String, image: ByteArray) =
            loggedInUser.apply {
                nickname = name
                imageByteArray = image
                bitmap = if (image.isEmpty()) null else imageProcessor.getBitmapFromByteArray(image)
            }
}