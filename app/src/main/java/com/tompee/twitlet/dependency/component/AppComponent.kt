package com.tompee.twitlet.dependency.component

import android.content.Context
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.dependency.module.AppModule
import com.tompee.twitlet.dependency.module.SchedulerModule
import dagger.Component
import io.reactivex.Scheduler
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, SchedulerModule::class])
interface AppComponent {

    fun context(): Context

    fun application(): TwitletApplication

    @Named("io")
    fun ioScheduler(): Scheduler

    @Named("ui")
    fun uiScheduler(): Scheduler

}