package com.tompee.twitlet.feature.timeline.logout

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.interactor.TimelineInteractor

class LogoutPresenter(timelineInteractor: TimelineInteractor,
                      private val schedulers: Schedulers) :
        BasePresenter<LogoutView, TimelineInteractor>(timelineInteractor) {

    override fun onViewAttached(view: LogoutView) {
        addSubscription(view.logout()
                .observeOn(schedulers.io)
                .flatMapCompletable {
                    interactor.logout()
                            .observeOn(schedulers.ui)
                            .doOnComplete {
                                view.dismiss()
                                view.moveToLoginScreen()
                            }
                }
                .onErrorComplete()
                .subscribe())
    }

    override fun onViewDetached() {
    }
}