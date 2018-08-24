package com.tompee.twitlet.feature.login.page

import com.tompee.twitlet.base.BaseView
import io.reactivex.Observable

interface LoginPageView : BaseView {
    fun getEmail(): Observable<String>
    fun getPassword(): Observable<String>

    fun showEmptyEmailError()
    fun showInvalidEmailError()
    fun clearEmailError()
    fun showEmptyPasswordError()
    fun showPasswordShortError()
    fun clearPasswordError()

}