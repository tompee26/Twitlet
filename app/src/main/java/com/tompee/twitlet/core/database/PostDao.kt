package com.tompee.twitlet.core.database

import com.tompee.twitlet.model.Post
import io.reactivex.Completable

interface PostDao {
    fun savePost(post: Post, user: UserEntity): Completable
}