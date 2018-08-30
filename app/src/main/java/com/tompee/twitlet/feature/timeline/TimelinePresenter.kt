package com.tompee.twitlet.feature.timeline

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.interactor.DataInteractor
import com.tompee.twitlet.model.User

class TimelinePresenter(private val dataInteractor: DataInteractor,
                        private val user: User) : BasePresenter<TimelineView>() {
    override fun onViewAttached(view: TimelineView) {
        dataInteractor.getPosts(user)
                .map { TimelineAdapter(it, user) }
                .subscribe { view.setAdapter(it) }
    }

    override fun onViewDetached() {
    }
}