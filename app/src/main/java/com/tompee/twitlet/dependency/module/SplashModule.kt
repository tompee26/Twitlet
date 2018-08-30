package com.tompee.twitlet.dependency.module

import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.core.auth.Authenticator
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.image.ImageProcessor
import com.tompee.twitlet.feature.splash.SplashPresenter
import com.tompee.twitlet.interactor.SplashInteractor
import com.tompee.twitlet.model.User
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @Provides
    fun provideSplashPresenter(splashInteractor: SplashInteractor,
                               schedulers: Schedulers): SplashPresenter =
            SplashPresenter(splashInteractor, schedulers)

    @Provides
    fun provideSplashInteractor(authenticator: Authenticator,
                                userDao: UserDao,
                                imageProcessor: ImageProcessor,
                                user: User) =
            SplashInteractor(authenticator, userDao, imageProcessor, user)
}