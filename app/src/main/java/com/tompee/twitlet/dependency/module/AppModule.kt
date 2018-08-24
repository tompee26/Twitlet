package com.tompee.twitlet.dependency.module

import android.content.Context
import com.tompee.twitlet.TwitletApplication
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
}