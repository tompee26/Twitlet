package com.tompee.twitlet.core.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Single

class FirebaseStorage(private val storage: FirebaseStorage) : Storage {

    companion object {
        private const val PROFILE = "profile"
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
}