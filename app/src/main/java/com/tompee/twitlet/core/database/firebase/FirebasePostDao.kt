package com.tompee.twitlet.core.database.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tompee.twitlet.core.database.PostDao
import com.tompee.twitlet.core.database.PostEntity
import com.tompee.twitlet.core.database.UserEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class FirebasePostDao(private val db: FirebaseFirestore) : PostDao {

    companion object {
        private const val POSTS = "posts"
    }

    override fun savePost(post: PostEntity, user: UserEntity): Completable =
            Completable.create { emitter ->
                db.collection(POSTS).document(post.time).set(post)
                emitter.onComplete()
            }

    override fun getPosts(): Observable<List<PostEntity>> =
            Observable.create { emitter ->
                db.collection(POSTS)
                        .orderBy("time", Query.Direction.DESCENDING)
                        .addSnapshotListener { value, error ->
                            if (error != null) {
                                emitter.onError(error)
                            }
                            val posts = ArrayList<PostEntity>()
                            value?.forEach { doc ->
                                posts.add(doc.toObject(PostEntity::class.java))
                            }
                            emitter.onNext(posts)
                        }
            }

    override fun getPost(postId: String): Single<PostEntity> =
            Single.create { emitter ->
                db.collection(POSTS).document(postId).get()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                emitter.onSuccess(it.result.toObject(PostEntity::class.java)!!)
                            } else {
                                emitter.onError(Throwable(it.exception?.message))
                            }
                        }
            }


    override fun deletePost(postId: String): Completable =
            Completable.create { emitter ->
                db.collection(POSTS).document(postId)
                        .delete()
                emitter.onComplete()
            }
}