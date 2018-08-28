package com.tompee.twitlet

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import com.tompee.twitlet.dependency.component.AppComponent
import com.tompee.twitlet.dependency.component.DaggerAppComponent
import com.tompee.twitlet.dependency.module.AppModule
import com.tompee.twitlet.dependency.module.AuthModule
import com.tompee.twitlet.dependency.module.DataModule
import com.tompee.twitlet.dependency.module.SchedulerModule
import timber.log.Timber

class TwitletApplication : MultiDexApplication() {

    val component: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .authModule(AuthModule())
                .schedulerModule(SchedulerModule())
                .dataModule(DataModule())
                .build()
    }

    companion object {
        operator fun get(context: Context): TwitletApplication =
                context.applicationContext as TwitletApplication
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        Timber.plant(Timber.DebugTree())
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}