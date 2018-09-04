package com.tompee.twitlet.feature.timeline.delete

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.interactor.TimelineInteractor
import timber.log.Timber

class DeletePresenter(timelineInteractor: TimelineInteractor,
                      private val schedulers: Schedulers) :
        BasePresenter<DeleteView, TimelineInteractor>(timelineInteractor) {

    override fun onViewAttached(view: DeleteView) {
        view.delete()
                .flatMapCompletable {
                    interactor.deletePost(view.postId(), view.imageUrl())
                            .observeOn(schedulers.ui)
                            .doOnComplete { view.dismiss() }
                }
                .subscribe({}, Timber::e)
    }

    override fun onViewDetached() {
    }
}