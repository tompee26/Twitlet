package com.tompee.twitlet.interactor

import android.net.Uri
import com.tompee.twitlet.base.BaseInteractor
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.database.UserEntity
import com.tompee.twitlet.core.storage.Storage
import com.tompee.twitlet.model.User
import io.reactivex.Completable

class ProfileInteractor(private val userDao: UserDao,
                        private val storage: Storage,
                        private val loggedInUser: User) : BaseInteractor {

    fun saveUser(name: String, url: String): Completable {
        return if (url.isNotEmpty()) {
            storage.uploadProfileImage(loggedInUser.email, Uri.parse(url))
                    .doOnSuccess { newUrl ->
                        loggedInUser.apply {
                            nickname = name
                            imageUrl = newUrl
                        }
                    }
                    .flatMapCompletable {
                        userDao.saveUser(UserEntity(loggedInUser.email, name, it))
                    }

        } else {
            userDao.saveUser(UserEntity(loggedInUser.email, name, url))
                    .doOnComplete {
                        loggedInUser.apply {
                            nickname = name
                            imageUrl = url
                        }
                    }
        }
    }
}