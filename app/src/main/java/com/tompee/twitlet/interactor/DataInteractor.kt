package com.tompee.twitlet.interactor

import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import com.tompee.twitlet.core.asset.AssetManager
import com.tompee.twitlet.core.database.PostDao
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.database.UserEntity
import com.tompee.twitlet.core.image.ImageProcessor
import com.tompee.twitlet.model.Post
import com.tompee.twitlet.model.User
import io.reactivex.Completable
import io.reactivex.Single

class DataInteractor(private val userDao: UserDao,
                     private val postDao: PostDao,
                     private val imageProcessor: ImageProcessor,
                     private val assetManager: AssetManager) {

    fun getUser(email: String): Single<User> {
        return userDao.getUser(email)
                .map {
                    User(email = it.email, nickname = it.nickname,
                            imageByteArray = Base64.decode(it.image, Base64.DEFAULT))
                }
    }

    fun saveUser(user: User): Completable {
        return userDao.saveUser(UserEntity(email = user.email,
                nickname = user.nickname, image = Base64.encodeToString(user.imageByteArray, Base64.DEFAULT)))
    }

    fun savePost(user: User, post: Post) = postDao.savePost(post, UserEntity(email = user.email))

    fun getBitmapFromUri(uri: Uri): Bitmap = imageProcessor.getBitmapFromUri(uri)

    fun getByteArrayFromBitmap(bitmap: Bitmap) = imageProcessor.getByteStreamFromBitmap(bitmap)

    fun getBitmapFromByteArray(byteArray: ByteArray): Bitmap? =
            if (byteArray.isEmpty()) null else imageProcessor.getBitmapFromByteArray(byteArray)

    fun getContentFromHtmlAsset(filename: String): String {
        return assetManager.getStringFromAsset(filename)
    }
}