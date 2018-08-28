package com.tompee.twitlet.dependency.module

import com.tompee.twitlet.feature.splash.SplashPresenter
import com.tompee.twitlet.interactor.AuthInteractor
import com.tompee.twitlet.interactor.DataInteractor
import com.tompee.twitlet.model.User
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class SplashModule {

    @Provides
    fun provideSplashPresenter(authInteractor: AuthInteractor,
                               dataInteractor: DataInteractor,
                               user: User,
                               @Named("io") io: Scheduler,
                               @Named("ui") ui: Scheduler): SplashPresenter =
            SplashPresenter(authInteractor, dataInteractor, user, io, ui)
}