package com.tompee.twitlet.dependency.module

import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.storage.Storage
import com.tompee.twitlet.feature.profile.ProfilePresenter
import com.tompee.twitlet.interactor.ProfileInteractor
import com.tompee.twitlet.model.User
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @Provides
    fun provideProfilePresenter(profileInteractor: ProfileInteractor,
                                schedulers: Schedulers): ProfilePresenter =
            ProfilePresenter(profileInteractor, schedulers)

    @Provides
    fun provideProfileInteractor(userDao: UserDao,
                                 storage: Storage,
                                 user: User): ProfileInteractor =
            ProfileInteractor(userDao, storage, user)
}