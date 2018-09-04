package com.tompee.twitlet.dependency.component

import com.tompee.twitlet.dependency.module.PostModule
import com.tompee.twitlet.dependency.scope.PostScope
import com.tompee.twitlet.feature.post.PostActivity
import dagger.Component

@PostScope
@Component(modules = [PostModule::class],
        dependencies = [AppComponent::class])
interface PostComponent {
    fun inject(postActivity: PostActivity)
}