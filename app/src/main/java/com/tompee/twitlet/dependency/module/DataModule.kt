package com.tompee.twitlet.dependency.module

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.database.firebase.FirebaseUserDao
import com.tompee.twitlet.interactor.DataInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideDataInteractor(userDao: UserDao): DataInteractor = DataInteractor(userDao)

    @Singleton
    @Provides
    fun provideUserDao(firebaseUserDao: FirebaseUserDao): UserDao = firebaseUserDao

    @Singleton
    @Provides
    fun provideFirebaseUserDao(db: FirebaseFirestore): FirebaseUserDao = FirebaseUserDao(db)

    @Singleton
    @Provides
    fun provideFirestoreDb(): FirebaseFirestore {
        val reference = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        reference.firestoreSettings = settings
        return reference
    }
}