package com.tompee.twitlet.feature.profile

import com.tompee.twitlet.base.BasePresenter

class ProfilePresenter : BasePresenter<ProfileView>() {
    override fun onViewAttached(view: ProfileView) {
        setupImageCropper()
    }

    override fun onViewDetached() {
    }

    private fun setupImageCropper() {
        addSubscription(view.profileImageRequest()
                .subscribe { view.startImageCropper() })
    }
}