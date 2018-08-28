package com.tompee.twitlet.feature.profile

import com.tompee.twitlet.base.BaseView
import io.reactivex.Observable

interface ProfileView : BaseView {
    fun profileImageRequest(): Observable<Any>

    fun startImageCropper()
}