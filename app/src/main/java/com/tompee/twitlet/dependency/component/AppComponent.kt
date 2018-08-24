package com.tompee.twitlet.dependency.component

import android.content.Context
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.dependency.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun context(): Context

    fun application(): TwitletApplication
}