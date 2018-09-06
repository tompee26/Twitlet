package com.tompee.twitlet.dependency.component

import android.content.Context
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.core.asset.AssetManager
import com.tompee.twitlet.core.auth.Authenticator
import com.tompee.twitlet.core.database.PostDao
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.core.database.firebase.FirebasePostDao
import com.tompee.twitlet.core.image.Renderer
import com.tompee.twitlet.core.storage.Storage
import com.tompee.twitlet.dependency.module.AppModule
import com.tompee.twitlet.dependency.module.AuthModule
import com.tompee.twitlet.dependency.module.DataModule
import com.tompee.twitlet.dependency.module.ImageModule
import com.tompee.twitlet.dependency.module.SchedulerModule
import com.tompee.twitlet.model.User
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, SchedulerModule::class, AuthModule::class, DataModule::class, ImageModule::class])
interface AppComponent {

    //region AppModule
    fun context(): Context

    fun application(): TwitletApplication

    fun user(): User

    fun assetManager(): AssetManager
    //endregion

    //region SchedulerModule
    fun schedulers(): Schedulers
    //endregion

    //region AuthModule
    fun authenticator(): Authenticator
    //endregion

    //region DataModule
    fun userDao(): UserDao

    fun postDao(): PostDao

    fun storage(): Storage

    fun firebasePostDao(): FirebasePostDao
    //endregion

    //region ImageModule
    fun renderer(): Renderer
    //endregion
}