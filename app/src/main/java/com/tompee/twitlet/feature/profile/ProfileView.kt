package com.tompee.twitlet.feature.profile

import android.net.Uri
import com.tompee.twitlet.base.BaseView
import io.reactivex.Observable

interface ProfileView : BaseView {
    fun profileImageRequest(): Observable<Any>
    fun photoUrl(): Observable<Uri>
    fun nickname(): Observable<String>
    fun saveProfile(): Observable<Any>

    fun showProgress()
    fun dismissProgress()
    fun startImageCropper()
    fun showEmptyNameError()
    fun moveToTimelineScreen()
    fun showError(message: String)
}