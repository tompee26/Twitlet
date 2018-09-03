package com.tompee.twitlet.feature.timeline

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.interactor.TimelineInteractor
import timber.log.Timber

class TimelinePresenter(timelineInteractor: TimelineInteractor,
                        private val schedulers: Schedulers,
                        private val timelineAdapter: TimelineAdapter) :
        BasePresenter<TimelineView, TimelineInteractor>(timelineInteractor) {

    override fun onViewAttached(view: TimelineView) {
        setUserProfile()
        populateTimeline()
    }

    override fun onViewDetached() {
    }

    private fun setUserProfile() {
        interactor.getUser()
                .subscribe(view::setUser)
    }

    private fun populateTimeline() {
        view.setAdapter(timelineAdapter)
        interactor.getPosts()
                .doOnNext { timelineAdapter.postList = it }
                .doOnNext { timelineAdapter.notifyDataSetChanged() }
                .subscribe({}, Timber::d)
    }
}