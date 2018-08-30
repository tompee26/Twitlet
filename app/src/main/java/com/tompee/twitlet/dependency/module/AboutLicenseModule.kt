package com.tompee.twitlet.dependency.module

import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.core.asset.AssetManager
import com.tompee.twitlet.feature.license.LicensePresenter
import com.tompee.twitlet.interactor.LicenseInteractor
import dagger.Module
import dagger.Provides

@Module
class AboutLicenseModule {
    @Provides
    fun provideLicensePresenter(licenseInteractor: LicenseInteractor,
                                scheduler: Schedulers): LicensePresenter =
            LicensePresenter(licenseInteractor, scheduler)

    @Provides
    fun provideLicenseInteractor(assetManager: AssetManager) =
            LicenseInteractor(assetManager)
}