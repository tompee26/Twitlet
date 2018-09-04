package com.tompee.twitlet.interactor

import com.tompee.twitlet.Constants.DATE_TIME_FORMAT
import com.tompee.twitlet.base.BaseInteractor
import com.tompee.twitlet.core.database.PostDao
import com.tompee.twitlet.model.Message
import com.tompee.twitlet.model.Post
import com.tompee.twitlet.model.User
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*

class PostInteractor(private val postDao: PostDao,
                     private val loggedInUser: User) : BaseInteractor {

    fun getPost(postId: String): Single<Post> = postDao.getPost(postId)
            .map {
                val format = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
                val message = Message(it.time, it.message, it.postImage, format.parse(it.time))
                val user = User(it.email, it.email == loggedInUser.email,
                        it.name, it.image)
                return@map Post(message, user)
            }

}