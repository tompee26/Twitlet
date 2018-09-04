package com.tompee.twitlet.core.database

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface PostDao {
    fun savePost(post: PostEntity, user: UserEntity): Completable

    fun getPosts(): Observable<List<PostEntity>>
    fun getPost(postId: String): Single<PostEntity>
    fun deletePost(postId: String): Completable
}