package com.tompee.twitlet.feature.license

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseActivity
import com.tompee.twitlet.dependency.component.DaggerAboutLicenseComponent
import com.tompee.twitlet.dependency.module.AboutLicenseModule
import kotlinx.android.synthetic.main.activity_license.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class LicenseActivity : BaseActivity(), LicenseView {

    @Inject
    lateinit var licensePresenter: LicensePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        toolbar_text.setText(R.string.label_license)
        licensePresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        licensePresenter.detachView()
    }

    override fun layoutId(): Int = R.layout.activity_license

    override fun setupComponent() {
        DaggerAboutLicenseComponent.builder()
                .appComponent(TwitletApplication[this].component)
                .aboutLicenseModule(AboutLicenseModule())
                .build()
                .inject(this)
    }

    override fun setLicenseText(text: String) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            content.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            content.text = Html.fromHtml(text)
        }
        content.movementMethod = LinkMovementMethod.getInstance()
    }
}