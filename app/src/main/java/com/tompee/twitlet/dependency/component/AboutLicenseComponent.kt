package com.tompee.twitlet.dependency.component

import com.tompee.twitlet.dependency.module.AboutLicenseModule
import com.tompee.twitlet.dependency.scope.AboutLicenseScope
import com.tompee.twitlet.feature.license.LicenseActivity
import dagger.Component

@AboutLicenseScope
@Component(modules = [AboutLicenseModule::class],
        dependencies = [AppComponent::class])
interface AboutLicenseComponent {
    fun inject(licenseActivity: LicenseActivity)
}