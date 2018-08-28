package com.tompee.twitlet.dependency.module

import com.tompee.twitlet.feature.profile.ProfilePresenter
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @Provides
    fun provideProfilePresenter(): ProfilePresenter = ProfilePresenter()
}