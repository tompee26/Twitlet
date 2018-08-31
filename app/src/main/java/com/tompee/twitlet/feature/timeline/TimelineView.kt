package com.tompee.twitlet.feature.timeline

import com.tompee.twitlet.base.BaseView
import com.tompee.twitlet.model.User

interface TimelineView : BaseView {
    fun setAdapter(adapter: TimelineAdapter)
    fun setUser(user: User)
}