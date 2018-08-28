package com.tompee.twitlet.dependency.module

import com.tompee.twitlet.model.User
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserModule {

    @Singleton
    @Provides
    fun provideUser(): User = User()
}