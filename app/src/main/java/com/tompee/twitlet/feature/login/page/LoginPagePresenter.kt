package com.tompee.twitlet.feature.login.page

import android.util.Patterns
import com.tompee.twitlet.Constants.MIN_PASS_COUNT
import com.tompee.twitlet.base.BasePresenter
import io.reactivex.Observable
import io.reactivex.Scheduler

class LoginPagePresenter(private val io: Scheduler,
                         private val ui: Scheduler) : BasePresenter<LoginPageView>() {

    override fun onViewAttached(view: LoginPageView) {
        validateEmail(view.getEmail())
        validatePassword(view.getPassword())
    }

    override fun onViewDetached() {
    }

    private fun validateEmail(email: Observable<String>) {
        addSubscription(email.map { it.trim() }
                .observeOn(ui)
                .subscribe {
                    when {
                        it.isEmpty() -> view.showEmptyEmailError()
                        !Patterns.EMAIL_ADDRESS.matcher(it).matches() -> view.showInvalidEmailError()
                        else -> view.clearEmailError()
                    }
                })
    }

    private fun validatePassword(pass: Observable<String>) {
        addSubscription(pass.map { it.trim() }
                .observeOn(ui)
                .subscribe {
                    when {
                        it.isEmpty() -> view.showEmptyPasswordError()
                        it.length < MIN_PASS_COUNT -> view.showPasswordShortError()
                        else -> view.clearPasswordError()
                    }
                })
    }
}