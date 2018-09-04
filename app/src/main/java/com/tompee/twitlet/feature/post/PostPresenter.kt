package com.tompee.twitlet.feature.post

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.interactor.PostInteractor
import timber.log.Timber


class PostPresenter(postInteractor: PostInteractor,
                    private val schedulers: Schedulers) :
        BasePresenter<PostView, PostInteractor>(postInteractor) {

    override fun onViewAttached(view: PostView) {
        setupPost()
    }

    override fun onViewDetached() {
    }

    private fun setupPost() {
        addSubscription(interactor.getPost(view.postId())
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.ui)
                .doOnSuccess {
                    view.setPost(it)
                }
                .subscribe({}, Timber::e))
    }

}