package com.tompee.twitlet.interactor

import com.tompee.twitlet.base.BaseInteractor
import com.tompee.twitlet.core.asset.AssetManager
import io.reactivex.Single

class LicenseInteractor(private val assetManager: AssetManager) : BaseInteractor {

    fun getContentFromHtmlAsset(filename: String): Single<String> {
        return Single.just(assetManager.getStringFromAsset(filename))
    }
}