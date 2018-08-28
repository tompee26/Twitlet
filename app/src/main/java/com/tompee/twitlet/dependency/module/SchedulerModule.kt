package com.tompee.twitlet.dependency.module

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class SchedulerModule {
    @Singleton
    @Provides
    @Named("io")
    fun provideIoScheduler(): Scheduler = Schedulers.io()

    @Singleton
    @Provides
    @Named("ui")
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Singleton
    @Provides
    @Named("co")
    fun provideComputationScheduler(): Scheduler = Schedulers.computation()

}