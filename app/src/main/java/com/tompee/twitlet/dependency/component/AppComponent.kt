package com.tompee.twitlet.dependency.component

import android.content.Context
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.dependency.module.AppModule
import com.tompee.twitlet.dependency.module.AuthModule
import com.tompee.twitlet.dependency.module.DataModule
import com.tompee.twitlet.dependency.module.SchedulerModule
import com.tompee.twitlet.dependency.module.UserModule
import com.tompee.twitlet.interactor.AuthInteractor
import com.tompee.twitlet.interactor.DataInteractor
import com.tompee.twitlet.model.User
import dagger.Component
import io.reactivex.Scheduler
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, SchedulerModule::class, AuthModule::class,
    UserModule::class, DataModule::class])
interface AppComponent {

    fun context(): Context

    fun application(): TwitletApplication

    @Named("io")
    fun ioScheduler(): Scheduler

    @Named("ui")
    fun uiScheduler(): Scheduler

    fun authInteractor(): AuthInteractor

    fun dataInteractor(): DataInteractor

    fun user(): User
}