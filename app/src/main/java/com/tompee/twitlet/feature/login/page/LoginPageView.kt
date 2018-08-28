package com.tompee.twitlet.feature.login.page

import com.tompee.twitlet.base.BaseView
import io.reactivex.Observable

interface LoginPageView : BaseView {
    fun getEmail(): Observable<String>
    fun getPassword(): Observable<String>
    fun getViewType(): Int
    fun command(): Observable<Any>

    fun showEmptyEmailError()
    fun showInvalidEmailError()
    fun clearEmailError()
    fun showEmptyPasswordError()
    fun showPasswordShortError()
    fun clearPasswordError()

    fun showProgressDialog()
    fun dismissProgressDialog()
    fun showSignupSuccessMessage()
    fun showError(message : String)

    fun moveToProfileScreen()
    fun moveToTimelineScreen()
}