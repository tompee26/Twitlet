package com.tompee.twitlet.core.database.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.database.UserEntity
import io.reactivex.Completable
import io.reactivex.Single

class FirebaseUserDao(private val db: FirebaseFirestore) : UserDao {

    companion object {
        private const val PROFILE = "profile"
    }

    override fun getUser(email: String): Single<UserEntity> {
        return Single.create<UserEntity> { emitter ->
            val docRef = db.collection(PROFILE).document(email)
            docRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    emitter.onSuccess(documentSnapshot.toObject(UserEntity::class.java)!!)
                } else {
                    emitter.onError(Throwable("User not found"))
                }
            }.addOnFailureListener { emitter.onError(Throwable(it.message)) }
        }
    }

    override fun saveUser(userEntity: UserEntity): Completable {
        return Completable.create { emitter ->
            db.collection(PROFILE).document(userEntity.email).set(userEntity)
                    .addOnSuccessListener {
                        emitter.onComplete()
                    }.addOnFailureListener {
                        emitter.onError(Throwable(it.message))
                    }
        }
    }
}