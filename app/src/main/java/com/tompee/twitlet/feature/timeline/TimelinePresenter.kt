package com.tompee.twitlet.feature.timeline

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.interactor.TimelineInteractor

class TimelinePresenter(timelineInteractor: TimelineInteractor,
                        private val schedulers: Schedulers) :
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
        interactor.getPosts()
                .map { TimelineAdapter(it) }
                .subscribe { view.setAdapter(it) }
    }
}