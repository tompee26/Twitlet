package com.tompee.twitlet.dependency.module

import android.arch.paging.PagedList
import android.support.v4.app.FragmentActivity
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.tompee.twitlet.Constants
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.core.auth.Authenticator
import com.tompee.twitlet.core.database.PostDao
import com.tompee.twitlet.core.database.PostEntity
import com.tompee.twitlet.core.database.firebase.FirebasePostDao
import com.tompee.twitlet.core.image.Renderer
import com.tompee.twitlet.core.storage.Storage
import com.tompee.twitlet.dependency.scope.TimelineScope
import com.tompee.twitlet.feature.timeline.TimelineAdapter
import com.tompee.twitlet.feature.timeline.TimelinePresenter
import com.tompee.twitlet.feature.timeline.delete.DeletePresenter
import com.tompee.twitlet.feature.timeline.logout.LogoutPresenter
import com.tompee.twitlet.feature.timeline.post.PostPresenter
import com.tompee.twitlet.interactor.TimelineInteractor
import com.tompee.twitlet.model.Message
import com.tompee.twitlet.model.Post
import com.tompee.twitlet.model.User
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.*

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
                                 timelineAdapter: TimelineAdapter): TimelinePresenter =
            TimelinePresenter(timelineInteractor, timelineAdapter)

    @Provides
    fun provideTimelineAdapter(options: FirestorePagingOptions<Post>,
                               renderer: Renderer): TimelineAdapter =
            TimelineAdapter(options, fragmentActivity,
                    fragmentActivity.supportFragmentManager, renderer)

    @TimelineScope
    @Provides
    fun provideTimelineInteractor(postDao: PostDao,
                                  authenticator: Authenticator,
                                  storage: Storage,
                                  user: User): TimelineInteractor =
            TimelineInteractor(postDao, authenticator, storage, user)

    @TimelineScope
    @Provides
    fun provideFirestorePagingOptions(firebasePostDao: FirebasePostDao,
                                      loggedInUser: User): FirestorePagingOptions<Post> {
        var config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(10)
                .build()

        return FirestorePagingOptions.Builder<Post>()
                .setLifecycleOwner(fragmentActivity)
                .setQuery(firebasePostDao.getPagingQuery(), config) {
                    val pe = it.toObject(PostEntity::class.java)!!
                    val format = SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.getDefault())
                    val message = Message(pe.time, pe.message, pe.postImage, format.parse(pe.time))
                    val user = User(pe.email, loggedInUser.email == pe.email, pe.name, pe.image)
                    Post(message, user)
                }
                .build()
    }
}