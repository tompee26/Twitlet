package com.tompee.twitlet.interactor

import android.util.Base64
import com.tompee.twitlet.base.BaseInteractor
import com.tompee.twitlet.core.auth.Authenticator
import com.tompee.twitlet.core.database.PostDao
import com.tompee.twitlet.core.database.PostEntity
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.database.UserEntity
import com.tompee.twitlet.core.image.ImageProcessor
import com.tompee.twitlet.model.Message
import com.tompee.twitlet.model.Post
import com.tompee.twitlet.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*

class TimelineInteractor(private val postDao: PostDao,
                         private val userDao: UserDao,
                         private val authenticator: Authenticator,
                         private val imageProcessor: ImageProcessor,
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
                        .concatMap { post ->
                            userDao.getUserImage(post.user.email)
                                    .map { Base64.decode(it, Base64.DEFAULT) }
                                    .map {
                                        post.user.bitmap = if (it.isEmpty()) null
                                        else imageProcessor.getBitmapFromByteArray(it)
                                        return@map post
                                    }
                                    .toObservable()
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