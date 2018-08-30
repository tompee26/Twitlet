package com.tompee.twitlet.feature.splash

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.interactor.AuthInteractor
import com.tompee.twitlet.interactor.DataInteractor
import com.tompee.twitlet.model.User
import io.reactivex.Scheduler
import timber.log.Timber

class SplashPresenter(private val authInteractor: AuthInteractor,
                      private val dataInteractor: DataInteractor,
                      private val user: User,
                      private val io: Scheduler,
                      private val ui: Scheduler) : BasePresenter<SplashView>() {

    override fun onViewAttached(view: SplashView) {
        val subscription = authInteractor.getCurrentUser()
                .doOnSuccess {
                    user.email = it
                    user.isAuthenticated = true
                }
                .observeOn(ui)
                .doOnError { view.moveToLoginScreen() }
                .observeOn(io)
                .flatMap { email ->
                    dataInteractor.getUser(email)
                            .doOnSuccess {
                                user.nickname = it.nickname
                                user.imageByteArray = it.imageByteArray
                                user.bitmap = dataInteractor.getBitmapFromByteArray(it.imageByteArray)
                            }
                            .observeOn(ui)
                            .doOnSuccess { view.moveToTimelineScreen() }
                            .doOnError { view.moveToProfileScreen() }
                            .subscribeOn(io)
                }
                .subscribeOn(io)
                .subscribe({ Timber.d(it.toString()) }, Timber::e)
        addSubscription(subscription)
    }

    override fun onViewDetached() {
    }
}