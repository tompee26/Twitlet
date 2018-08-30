package com.tompee.twitlet.feature.profile

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.interactor.DataInteractor
import com.tompee.twitlet.model.User
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.withLatestFrom
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ProfilePresenter(private val dataInteractor: DataInteractor,
                       private val user: User,
                       private val io: Scheduler,
                       private val ui: Scheduler,
                       private val co: Scheduler) : BasePresenter<ProfileView>() {
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
                .observeOn(ui)
                .subscribe { view.showEmptyNameError() }
    }

    private fun setupProfileSave() {
        val imageStream = Observable.just(ByteArray(0))
                .concatWith(view.photoUrl()
                        .observeOn(co)
                        .map { dataInteractor.getBitmapFromUri(it) }
                        .map { dataInteractor.getByteArrayFromBitmap(it) }
                )

        val subscription = view.saveProfile()
                .withLatestFrom(
                        Observable.just("").concatWith(view.nickname())) { _, name -> name }
                .observeOn(ui)
                .doOnNext { if (it.isEmpty()) view.showEmptyNameError() }
                .observeOn(co)
                .filter { it.isNotEmpty() }
                .withLatestFrom(imageStream) { name, image -> Pair(name, image) }
                .observeOn(ui)
                .doOnNext { view.showProgress() }
                .observeOn(co)
                .map {
                    user.nickname = it.first
                    user.imageByteArray = it.second
                    user.bitmap = dataInteractor.getBitmapFromByteArray(it.second)
                    return@map user
                }
                .flatMapCompletable { user ->
                    dataInteractor.saveUser(user)
                            .observeOn(ui)
                            .doOnComplete { view.moveToTimelineScreen() }
                            .doOnError { view.showError(it.message ?: "Error encountered") }
                            .onErrorComplete()
                            .doOnComplete { view.dismissProgress() }
                            .subscribeOn(io)
                }
                .subscribe({ Timber.d("Completed") }, Timber::e)
        addSubscription(subscription)
    }
}