package com.tompee.twitlet.dependency.module

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.tompee.twitlet.core.asset.AssetManager
import com.tompee.twitlet.core.database.PostDao
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.database.firebase.FirebasePostDao
import com.tompee.twitlet.core.database.firebase.FirebaseUserDao
import com.tompee.twitlet.core.image.ImageProcessor
import com.tompee.twitlet.interactor.DataInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideDataInteractor(userDao: UserDao,
                              postDao: PostDao,
                              imageProcessor: ImageProcessor,
                              assetManager: AssetManager): DataInteractor =
            DataInteractor(userDao, postDao, imageProcessor, assetManager)

    @Singleton
    @Provides
    fun provideUserDao(firebaseUserDao: FirebaseUserDao): UserDao = firebaseUserDao

    @Singleton
    @Provides
    fun providePostDao(firebasePostDao: FirebasePostDao): PostDao = firebasePostDao

    @Singleton
    @Provides
    fun provideFirebaseUserDao(db: FirebaseFirestore): FirebaseUserDao = FirebaseUserDao(db)

    @Singleton
    @Provides
    fun provideFirebasePostDao(db: FirebaseFirestore): FirebasePostDao = FirebasePostDao(db)

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