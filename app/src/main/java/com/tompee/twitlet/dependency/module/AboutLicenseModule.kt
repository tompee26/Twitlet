package com.tompee.twitlet.dependency.module

import com.tompee.twitlet.feature.license.LicensePresenter
import com.tompee.twitlet.interactor.DataInteractor
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class AboutLicenseModule {
    @Provides
    fun provideLicensePresenter(dataInteractor: DataInteractor,
                                @Named("io") io: Scheduler,
                                @Named("ui") ui: Scheduler): LicensePresenter =
            LicensePresenter(dataInteractor, io, ui)
}