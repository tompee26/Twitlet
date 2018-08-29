package com.tompee.twitlet.feature.license

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.interactor.DataInteractor
import io.reactivex.Scheduler
import io.reactivex.Single
import timber.log.Timber

class LicensePresenter(private val dataInteractor: DataInteractor,
                       private val io: Scheduler,
                       private val ui: Scheduler) : BasePresenter<LicenseView>() {
    override fun onViewAttached(view: LicenseView) {
        Single.just(dataInteractor.getContentFromHtmlAsset("opensource.html"))
                .subscribeOn(io)
                .observeOn(ui)
                .subscribe(view::setLicenseText, Timber::e)
    }

    override fun onViewDetached() {
    }
}