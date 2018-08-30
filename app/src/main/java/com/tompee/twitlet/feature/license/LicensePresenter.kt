package com.tompee.twitlet.feature.license

import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.interactor.LicenseInteractor
import timber.log.Timber

class LicensePresenter(licenseInteractor: LicenseInteractor,
                       private val schedulers: Schedulers) :
        BasePresenter<LicenseView, LicenseInteractor>(licenseInteractor) {

    override fun onViewAttached(view: LicenseView) {
        interactor.getContentFromHtmlAsset("opensource.html")
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.ui)
                .subscribe(view::setLicenseText, Timber::e)
    }

    override fun onViewDetached() {
    }
}