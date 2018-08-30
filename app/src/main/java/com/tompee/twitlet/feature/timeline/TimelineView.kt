package com.tompee.twitlet.feature.timeline

import com.tompee.twitlet.base.BaseView

interface TimelineView : BaseView {
    fun setAdapter(adapter: TimelineAdapter)
}