package com.tompee.twitlet.feature.profile

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.interactor.ProfileInteractor
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ProfilePresenter(profileInteractor: ProfileInteractor,
                       private val schedulers: Schedulers) :
        BasePresenter<ProfileView, ProfileInteractor>(profileInteractor) {

    override fun onViewAttached(view: ProfileView) {
        setupImageCropper()
        setupErrorChecking()
        setupProfileSave()
    }

    override fun onViewDetached() {
    }

    private fun setupImageCropper() {
        addSubscription(view.profileImageRequest()
                .subscribe { view.startImageCropper() })
    }

    private fun setupErrorChecking() {
        view.nickname()
                .debounce(1, TimeUnit.SECONDS)
                .filter { it.isEmpty() }
                .observeOn(schedulers.ui)
                .subscribe { view.showEmptyNameError() }
    }

    private fun setupProfileSave() {
        val imageStream = Observable.just("")
                .concatWith(view.photoUrl()
                        .observeOn(schedulers.computation)
                )

        val subscription = view.saveProfile()
                .withLatestFrom(
                        Observable.just("").concatWith(view.nickname())) { _, name -> name }
                .observeOn(schedulers.ui)
                .doOnNext { if (it.isEmpty()) view.showEmptyNameError() }
                .observeOn(schedulers.computation)
                .filter { it.isNotEmpty() }
                .withLatestFrom(imageStream) { name, image -> Pair(name, image) }
                .observeOn(schedulers.ui)
                .doOnNext { view.showProgress() }
                .observeOn(schedulers.computation)
                .flatMapCompletable { pair ->
                    interactor.saveUser(pair.first, pair.second)
                            .observeOn(schedulers.ui)
                            .doOnComplete { view.moveToTimelineScreen() }
                            .doOnError { view.showError(it.message ?: "Error encountered") }
                            .onErrorComplete()
                            .doOnComplete { view.dismissProgress() }
                            .subscribeOn(schedulers.io)
                }
                .subscribe({ Timber.d("Completed") }, Timber::e)
        addSubscription(subscription)
    }
}