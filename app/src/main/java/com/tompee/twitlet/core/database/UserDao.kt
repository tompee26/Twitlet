package com.tompee.twitlet.core.database

import io.reactivex.Completable
import io.reactivex.Single

interface UserDao {
    fun getUser(email: String): Single<UserEntity>

    fun saveUser(userEntity: UserEntity): Completable
}