package com.tompee.twitlet.core.auth.firebase

import com.google.firebase.auth.FirebaseAuth
import com.tompee.twitlet.core.auth.Authenticator
import io.reactivex.Completable
import io.reactivex.Single

class FirebaseAuthenticator(private val firebaseAuth: FirebaseAuth) : Authenticator {

    override fun signup(email: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(Throwable(it.exception?.message))
                        }
                    }
        }.andThen(Completable.create { emitter ->
            firebaseAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(Throwable(it.exception?.message))
                }
            }
        }).andThen(Completable.fromAction { firebaseAuth.signOut() })
    }

    override fun login(email: String, password: String): Single<String> =
            Single.create<String> { emitter ->
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onSuccess(firebaseAuth.currentUser?.email!!)
                    } else {
                        emitter.onError(Throwable(it.exception?.message))
                    }
                }
            }
}