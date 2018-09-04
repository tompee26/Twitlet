package com.tompee.twitlet.core.storage.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.tompee.twitlet.core.storage.Storage
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File

class FirebaseStorage(private val storage: FirebaseStorage) : Storage {

    companion object {
        private const val PROFILE = "profile"
        private const val POST = "post"
        private const val FILENAME = "profile.jpg"
    }

    override fun uploadProfileImage(email: String, uri: Uri): Single<String> {
        return Single.create<String> { emitter ->
            val storageRef = storage.getReference(PROFILE).child(email).child(FILENAME)
            storageRef.putFile(uri).continueWithTask { storageRef.downloadUrl }
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            emitter.onSuccess(it.result.toString())
                        } else {
                            emitter.onError(Throwable(it.exception?.message))
                        }
                    }
        }
    }

    override fun uploadPostImage(path: String, email: String, postId: String): Single<String> {
        return Single.create { emitter ->
            if (path.isEmpty()) {
                emitter.onSuccess("")
                return@create
            }
            val storageRef = storage.getReference(POST).child(email).child("$postId.jpg")
            storageRef.putFile(Uri.fromFile(File(path))).continueWithTask { storageRef.downloadUrl }
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            emitter.onSuccess(it.result.toString())
                        } else {
                            emitter.onError(Throwable(it.exception?.message))
                        }
                    }
        }
    }

    override fun deletePostImage(postImage: String): Completable {
        return Completable.create { emitter ->
            if (postImage.isEmpty()) {
                emitter.onComplete()
                return@create
            }
            val storageRef = storage.getReferenceFromUrl(postImage)
            storageRef.delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(Throwable(it.exception?.message))
                }
            }
        }
    }

}