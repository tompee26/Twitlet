package com.tompee.twitlet.core.database.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.tompee.twitlet.core.database.PostDao
import com.tompee.twitlet.core.database.UserEntity
import com.tompee.twitlet.model.Post
import io.reactivex.Completable

class FirebasePostDao(private val db: FirebaseFirestore) : PostDao {
    companion object {
        private const val PROFILE = "profile"
        private const val POSTS = "posts"
    }

    override fun savePost(post: Post, user: UserEntity): Completable {
        return Completable.create { emitter ->
            db.collection(PROFILE).document(user.email).collection(POSTS).document(post.time).set(post)
            emitter.onComplete()
        }
    }
}