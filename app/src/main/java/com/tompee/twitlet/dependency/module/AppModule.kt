package com.tompee.twitlet.dependency.module

import android.content.Context
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.core.asset.AssetManager
import com.tompee.twitlet.model.User
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: TwitletApplication) {

    @Singleton
    @Provides
    fun provideContext(): Context = application

    @Singleton
    @Provides
    fun provideApplication(): TwitletApplication = application

    @Singleton
    @Provides
    fun provideUser(): User = User()

    @Singleton
    @Provides
    fun provideAssetManager(): AssetManager = AssetManager(application)
}