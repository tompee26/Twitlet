package com.tompee.twitlet.dependency.module

import com.google.firebase.auth.FirebaseAuth
import com.tompee.twitlet.core.auth.Authenticator
import com.tompee.twitlet.core.auth.firebase.FirebaseAuthenticator
import com.tompee.twitlet.interactor.AuthInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AuthModule {

    @Singleton
    @Provides
    fun provideAuthInteractor(authenticator: Authenticator): AuthInteractor = AuthInteractor(authenticator)

    @Singleton
    @Provides
    fun provideAuthenticator(authenticator: FirebaseAuthenticator): Authenticator = authenticator

    @Singleton
    @Provides
    fun provideFirebaseAuthenticator(): FirebaseAuthenticator = FirebaseAuthenticator(FirebaseAuth.getInstance())
}