package com.tompee.twitlet.dependency.component

import android.content.Context
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.core.asset.AssetManager
import com.tompee.twitlet.core.image.ImageProcessor
import com.tompee.twitlet.dependency.module.AppModule
import com.tompee.twitlet.dependency.module.AuthModule
import com.tompee.twitlet.dependency.module.DataModule
import com.tompee.twitlet.dependency.module.SchedulerModule
import com.tompee.twitlet.interactor.AuthInteractor
import com.tompee.twitlet.interactor.DataInteractor
import com.tompee.twitlet.model.User
import dagger.Component
import io.reactivex.Scheduler
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, SchedulerModule::class, AuthModule::class, DataModule::class])
interface AppComponent {

    //region AppModule
    fun context(): Context

    fun application(): TwitletApplication

    fun user(): User

    fun imageProcessor(): ImageProcessor

    fun assetManager(): AssetManager
    //endregion

    //region SchedulerModule
    @Named("io")
    fun ioScheduler(): Scheduler

    @Named("ui")
    fun uiScheduler(): Scheduler

    @Named("co")
    fun computationScheduler(): Scheduler
    //endregion

    fun authInteractor(): AuthInteractor

    fun dataInteractor(): DataInteractor
}