package com.tompee.twitlet.interactor

import android.util.Base64
import com.tompee.twitlet.base.BaseInteractor
import com.tompee.twitlet.core.auth.Authenticator
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.image.ImageProcessor
import com.tompee.twitlet.model.User
import io.reactivex.Single

class SplashInteractor(private val authenticator: Authenticator,
                       private val userDao: UserDao,
                       private val imageProcessor: ImageProcessor,
                       private val loggedInUser: User) : BaseInteractor {

    fun getCurrentUser(): Single<String> = authenticator.getCurrentUser()
            .doOnSuccess {
                loggedInUser.apply {
                    email = it
                    isAuthenticated = true
                }
            }

    fun getUserInfo(email: String): Single<User> {
        return userDao.getUser(email)
                .map {
                    User(it.email, false, it.nickname,
                            Base64.decode(it.image, Base64.DEFAULT))
                }
                .doOnSuccess {
                    loggedInUser.apply {
                        nickname = it.nickname
                        imageByteArray = it.imageByteArray
                        bitmap = if (it.imageByteArray.isEmpty()) null else imageProcessor.getBitmapFromByteArray(it.imageByteArray)
                    }
                }
    }
}