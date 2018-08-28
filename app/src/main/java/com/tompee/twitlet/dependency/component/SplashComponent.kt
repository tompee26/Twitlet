package com.tompee.twitlet.dependency.component

import com.tompee.twitlet.dependency.module.SplashModule
import com.tompee.twitlet.dependency.scope.SplashScope
import com.tompee.twitlet.feature.splash.SplashActivity
import dagger.Component

@SplashScope
@Component(modules = [SplashModule::class],
        dependencies = [AppComponent::class])
interface SplashComponent {
    fun inject(splashActivity: SplashActivity)
}