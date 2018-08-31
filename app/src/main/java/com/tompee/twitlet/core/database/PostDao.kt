package com.tompee.twitlet.core.database

import io.reactivex.Completable
import io.reactivex.Observable

interface PostDao {
    fun savePost(post: PostEntity, user: UserEntity): Completable

    fun getPosts(): Observable<List<PostEntity>>
}