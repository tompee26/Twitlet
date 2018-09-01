package com.tompee.twitlet.feature.timeline.logout

import com.tompee.twitlet.base.BaseView
import io.reactivex.Observable

interface LogoutView : BaseView {
    fun logout(): Observable<Any>

    fun dismiss()
    fun moveToLoginScreen()
}