package com.tompee.twitlet.feature.splash

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.interactor.SplashInteractor
import timber.log.Timber

class SplashPresenter(splashInteractor: SplashInteractor,
                      private val schedulers: Schedulers) :
        BasePresenter<SplashView, SplashInteractor>(splashInteractor) {

    override fun onViewAttached(view: SplashView) {
        val subscription = interactor.getCurrentUser()
                .observeOn(schedulers.ui)
                .doOnError { view.moveToLoginScreen() }
                .observeOn(schedulers.io)
                .flatMap { email ->
                    interactor.getUserInfo(email)
                            .observeOn(schedulers.ui)
                            .doOnSuccess { view.moveToTimelineScreen() }
                            .doOnError { view.moveToProfileScreen() }
                            .subscribeOn(schedulers.io)
                }
                .subscribeOn(schedulers.io)
                .subscribe({ Timber.d(it.toString()) }, Timber::e)
        addSubscription(subscription)
    }

    override fun onViewDetached() {
    }
}