package com.tompee.twitlet.feature.timeline.post

import com.tompee.twitlet.base.BaseView
import com.tompee.twitlet.model.User
import io.reactivex.Observable

interface PostView : BaseView {
    fun message(): Observable<String>
    fun post(): Observable<Any>

    fun setUser(user: User)
    fun dismiss()
}