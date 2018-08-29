package com.tompee.twitlet.dependency.component

import com.tompee.twitlet.dependency.module.TimelineModule
import com.tompee.twitlet.dependency.scope.TimelineScope
import com.tompee.twitlet.feature.timeline.post.PostDialog
import dagger.Component

@TimelineScope
@Component(modules = [TimelineModule::class],
        dependencies = [AppComponent::class])
interface TimelineComponent {
    fun inject(postDialog: PostDialog)
}