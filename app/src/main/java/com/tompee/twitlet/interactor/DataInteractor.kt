package com.tompee.twitlet.interactor

import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.database.UserEntity
import com.tompee.twitlet.model.User
import io.reactivex.Completable
import io.reactivex.Single

class DataInteractor(private val userDao: UserDao) {

    fun getUser(email: String): Single<User> {
        return userDao.getUser(email)
                .map { User(email = it.email, nickname = it.nickname, image = it.image) }
    }

    fun saveUser(user: User): Completable {
        return userDao.saveUser(UserEntity(email = user.email,
                nickname = user.nickname, image = user.image))
    }
}