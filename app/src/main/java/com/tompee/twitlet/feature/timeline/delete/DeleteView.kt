package com.tompee.twitlet.feature.timeline.delete

import com.tompee.twitlet.base.BaseView
import io.reactivex.Observable

interface DeleteView : BaseView {
    fun delete(): Observable<Any>

    fun dismiss()
    fun postId(): String
}