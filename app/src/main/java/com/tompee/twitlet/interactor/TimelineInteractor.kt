package com.tompee.twitlet.interactor

import com.tompee.twitlet.base.BaseInteractor
import com.tompee.twitlet.core.auth.Authenticator
import com.tompee.twitlet.core.database.PostDao
import com.tompee.twitlet.core.database.PostEntity
import com.tompee.twitlet.core.database.UserEntity
import com.tompee.twitlet.model.Message
import com.tompee.twitlet.model.Post
import com.tompee.twitlet.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*

class TimelineInteractor(private val postDao: PostDao,
                         private val authenticator: Authenticator,
                         private val loggedInUser: User) : BaseInteractor {
    companion object {
        private const val DATE_TIME_FORMAT = "yyyyMMddHHmmss"
    }

    fun saveMessage(message: Message): Completable {
        val format = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        return postDao.savePost(PostEntity(loggedInUser.email, loggedInUser.nickname,
                message.message, format.format(message.time)),
                UserEntity(loggedInUser.email, loggedInUser.nickname, ""))
    }

    fun getUser(): Single<User> = Single.just(loggedInUser)

    fun getPosts(): Observable<List<Post>> = postDao.getPosts()
            .flatMap { list ->
                Observable.fromIterable(list)
                        .map {
                            val format = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
                            val message = Message(it.message, format.parse(it.time))
                            val user = User(email = it.email, nickname = it.name)
                            return@map Post(message, user)
                        }
                        .toList()
                        .toObservable()
            }

    fun logout(): Completable =
            authenticator.logout()
                    .doOnComplete {
                        loggedInUser.isAuthenticated = false
                    }
}