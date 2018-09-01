package com.tompee.twitlet.interactor

import com.tompee.twitlet.base.BaseInteractor
import com.tompee.twitlet.core.auth.Authenticator
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.model.User
import io.reactivex.Single

class LoginInteractor(private val authenticator: Authenticator,
                      private val userDao: UserDao,
                      private val loggedInUser: User) : BaseInteractor {

    fun signup(email: String, pass: String) = authenticator.signup(email, pass)

    fun login(email: String, pass: String): Single<String> =
            authenticator.login(email, pass)
                    .doOnSuccess {
                        loggedInUser.apply {
                            this.email = it
                            isAuthenticated = true
                        }
                    }

    fun getUserInfo(email: String): Single<User> {
        return userDao.getUser(email)
                .map { User(it.email, false, it.nickname, it.image) }
                .doOnSuccess {
                    loggedInUser.apply {
                        nickname = it.nickname
                        imageUrl = it.imageUrl
                    }
                }
    }
}