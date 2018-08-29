package com.tompee.twitlet.dependency.module

import com.tompee.twitlet.feature.timeline.post.PostPresenter
import com.tompee.twitlet.interactor.DataInteractor
import com.tompee.twitlet.model.User
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class TimelineModule {

    @Provides
    fun providePostPresenter(dataInteractor: DataInteractor,
                             user: User,
                             @Named("io") io: Scheduler,
                             @Named("ui") ui: Scheduler): PostPresenter =
            PostPresenter(dataInteractor, user, io, ui)

}