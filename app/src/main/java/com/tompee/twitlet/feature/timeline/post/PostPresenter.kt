package com.tompee.twitlet.feature.timeline.post

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.interactor.DataInteractor
import com.tompee.twitlet.model.Post
import com.tompee.twitlet.model.User
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.withLatestFrom
import timber.log.Timber
import java.util.*

class PostPresenter(private val dataInteractor: DataInteractor,
                    private val user: User,
                    private val io: Scheduler,
                    private val ui: Scheduler) : BasePresenter<PostView>() {
    override fun onViewAttached(view: PostView) {
        displayUserInfo()
        setupPostHandler()
    }

    override fun onViewDetached() {
    }

    private fun displayUserInfo() {
        view.setUser(user)
    }

    private fun setupPostHandler() {
        view.post()
                .observeOn(io)
                .withLatestFrom(view.message()) { _, message -> message }
                .filter { it.isNotEmpty() }
                .map { Post(it, Calendar.getInstance().time) }
                .flatMapCompletable {
                    dataInteractor.savePost(user, it)
                            .observeOn(ui)
                            .doOnComplete { view.dismiss() }
                }
                .subscribe({ Timber.d("completed") }, Timber::e)
    }
}