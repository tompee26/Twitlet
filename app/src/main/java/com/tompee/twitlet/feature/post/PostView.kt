package com.tompee.twitlet.feature.post

import com.tompee.twitlet.base.BaseView
import com.tompee.twitlet.model.Post

interface PostView : BaseView {
    fun postId(): String
    fun setPost(post: Post)
}