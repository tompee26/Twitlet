package com.tompee.twitlet.dependency.module

import com.tompee.twitlet.base.Schedulers
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Singleton

@Module
class SchedulerModule {
    @Singleton
    @Provides
    fun provideSchedulers(): Schedulers = Schedulers(io.reactivex.schedulers.Schedulers.io(),
            AndroidSchedulers.mainThread(), io.reactivex.schedulers.Schedulers.computation())
}