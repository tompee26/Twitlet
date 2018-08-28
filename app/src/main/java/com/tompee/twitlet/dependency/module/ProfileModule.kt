package com.tompee.twitlet.dependency.module

import com.tompee.twitlet.feature.profile.ProfilePresenter
import com.tompee.twitlet.interactor.DataInteractor
import com.tompee.twitlet.model.User
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class ProfileModule {

    @Provides
    fun provideProfilePresenter(dataInteractor: DataInteractor,
                                user: User,
                                @Named("io") io: Scheduler,
                                @Named("ui") ui: Scheduler,
                                @Named("co") co: Scheduler): ProfilePresenter =
            ProfilePresenter(dataInteractor, user, io, ui, co)
}