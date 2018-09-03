package com.tompee.twitlet.feature.timeline.post

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.interactor.TimelineInteractor
import com.tompee.twitlet.model.Message
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
        view.post()
                .observeOn(schedulers.io)
                .withLatestFrom(view.message()) { _, message -> message }
                .filter { it.isNotEmpty() }
                .map { Message("", it, Calendar.getInstance().time) }
                .flatMapCompletable {
                    interactor.saveMessage(it)
                            .observeOn(schedulers.ui)
                            .doOnComplete { view.dismiss() }
                }
                .subscribe({ Timber.d("completed") }, Timber::e)
    }
}