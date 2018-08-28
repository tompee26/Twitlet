package com.tompee.twitlet.dependency.component

import com.tompee.twitlet.dependency.module.ProfileModule
import com.tompee.twitlet.dependency.scope.ProfileScope
import com.tompee.twitlet.feature.profile.ProfileActivity
import dagger.Component

@ProfileScope
@Component(modules = [ProfileModule::class],
        dependencies = [AppComponent::class])
interface ProfileComponent {
    fun inject(activity: ProfileActivity)
}