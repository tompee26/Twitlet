package com.tompee.twitlet.feature.timeline

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.interactor.TimelineInteractor

class TimelinePresenter(timelineInteractor: TimelineInteractor,
                        private val schedulers: Schedulers) :
        BasePresenter<TimelineView, TimelineInteractor>(timelineInteractor) {

    override fun onViewAttached(view: TimelineView) {
//        interactor.getPosts()
//                .map { TimelineAdapter(it, user) }
//                .subscribe { view.setAdapter(it) }
    }

    override fun onViewDetached() {
    }
}