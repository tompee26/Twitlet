package com.tompee.twitlet.feature.timeline.post

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.interactor.TimelineInteractor
import com.tompee.twitlet.model.Message
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import timber.log.Timber
import java.util.*

class PostPresenter(timelineInteractor: TimelineInteractor,
                    private val schedulers: Schedulers) :
        BasePresenter<PostView, TimelineInteractor>(timelineInteractor) {

    override fun onViewAttached(view: PostView) {
        displayUserInfo()
        setupPostHandler()
    }

    override fun onViewDetached() {
    }

    private fun displayUserInfo() {
        interactor.getUser()
                .subscribe(view::setUser)
    }

    private fun setupPostHandler() {
        val imageObservable = Observable.just("")
                .concatWith(view.image())

        view.post()
                .observeOn(schedulers.io)
                .withLatestFrom(view.message(), imageObservable) { _, message, image -> Pair(message, image) }
                .filter { it.first.isNotEmpty() || it.second.isNotEmpty() }
                .map { Message("", it.first, it.second, Calendar.getInstance().time) }
                .observeOn(schedulers.ui)
                .doOnNext { view.showProgressDialog() }
                .observeOn(schedulers.io)
                .flatMapCompletable {
                    interactor.saveMessage(it)
                            .observeOn(schedulers.ui)
                            .doOnComplete { view.dismissProgressDialog() }
                            .doOnComplete { view.dismiss() }
                }
                .subscribe({ Timber.d("completed") }, Timber::e)
    }
}