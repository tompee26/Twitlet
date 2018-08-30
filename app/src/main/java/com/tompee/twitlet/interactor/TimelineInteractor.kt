package com.tompee.twitlet.interactor

import com.tompee.twitlet.base.BaseInteractor
import com.tompee.twitlet.core.database.PostDao
import com.tompee.twitlet.core.database.PostEntity
import com.tompee.twitlet.core.database.UserEntity
import com.tompee.twitlet.model.Post
import com.tompee.twitlet.model.User
import io.reactivex.Completable
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*

class TimelineInteractor(private val postDao: PostDao,
                         private val loggedInUser: User) : BaseInteractor {
    companion object {
        private const val DATE_TIME_FORMAT = "yyyyMMddHHmmss"
    }

    fun savePost(post: Post): Completable {
        val format = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        return postDao.savePost(PostEntity(post.message, format.format(post.time)),
                UserEntity(loggedInUser.email, loggedInUser.nickname, ""))
    }

    fun getUser(): Single<User> = Single.just(loggedInUser)
}