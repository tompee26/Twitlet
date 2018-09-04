package com.tompee.twitlet.dependency.module

import android.support.v4.app.FragmentActivity
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.core.auth.Authenticator
import com.tompee.twitlet.core.database.PostDao
import com.tompee.twitlet.core.storage.Storage
import com.tompee.twitlet.dependency.scope.TimelineScope
import com.tompee.twitlet.feature.timeline.TimelineAdapter
import com.tompee.twitlet.feature.timeline.TimelinePresenter
import com.tompee.twitlet.feature.timeline.delete.DeletePresenter
import com.tompee.twitlet.feature.timeline.logout.LogoutPresenter
import com.tompee.twitlet.feature.timeline.post.PostPresenter
import com.tompee.twitlet.interactor.TimelineInteractor
import com.tompee.twitlet.model.User
import dagger.Module
import dagger.Provides

@Module
class TimelineModule(private val fragmentActivity: FragmentActivity) {
    @Provides
    fun provideDeletePresenter(timelineInteractor: TimelineInteractor,
                               schedulers: Schedulers): DeletePresenter =
            DeletePresenter(timelineInteractor, schedulers)

    @Provides
    fun provideLogoutPresenter(timelineInteractor: TimelineInteractor,
                               schedulers: Schedulers): LogoutPresenter =
            LogoutPresenter(timelineInteractor, schedulers)

    @Provides
    fun providePostPresenter(timelineInteractor: TimelineInteractor,
                             schedulers: Schedulers): PostPresenter =
            PostPresenter(timelineInteractor, schedulers)

    @Provides
    fun provideTimelinePresenter(timelineInteractor: TimelineInteractor,
                                 timelineAdapter: TimelineAdapter,
                                 schedulers: Schedulers): TimelinePresenter =
            TimelinePresenter(timelineInteractor, schedulers, timelineAdapter)

    @Provides
    fun provideTimelineAdapter(): TimelineAdapter = TimelineAdapter(fragmentActivity,
            fragmentActivity.supportFragmentManager)

    @TimelineScope
    @Provides
    fun provideTimelineInteractor(postDao: PostDao,
                                  authenticator: Authenticator,
                                  storage: Storage,
                                  user: User): TimelineInteractor =
            TimelineInteractor(postDao, authenticator, storage, user)
}