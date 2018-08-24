package com.tompee.twitlet

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.tompee.twitlet.dependency.component.AppComponent
import com.tompee.twitlet.dependency.component.DaggerAppComponent
import com.tompee.twitlet.dependency.module.AppModule

class TwitletApplication : MultiDexApplication() {

    val component: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    companion object {
        operator fun get(context: Context): TwitletApplication =
                context.applicationContext as TwitletApplication
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}