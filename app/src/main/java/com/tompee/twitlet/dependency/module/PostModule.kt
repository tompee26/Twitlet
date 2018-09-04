package com.tompee.twitlet.dependency.module

import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.core.database.PostDao
import com.tompee.twitlet.feature.post.PostPresenter
import com.tompee.twitlet.interactor.PostInteractor
import com.tompee.twitlet.model.User
import dagger.Module
import dagger.Provides

@Module
class PostModule {
    @Provides
    fun providePostPresenter(postInteractor: PostInteractor,
                             schedulers: Schedulers): PostPresenter = PostPresenter(postInteractor, schedulers)

    @Provides
    fun providePostInteractor(postDao: PostDao,
                              user: User): PostInteractor =
            PostInteractor(postDao, user)
}