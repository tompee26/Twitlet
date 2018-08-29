package com.tompee.twitlet.feature.about

import android.os.Bundle
import com.tompee.twitlet.BuildConfig
import com.tompee.twitlet.R
import com.tompee.twitlet.base.BaseActivity
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.toolbar.*

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        toolbar_text.setText(R.string.label_about)
        version.text = String.format(getString(R.string.message_version), BuildConfig.VERSION_NAME)
    }

    override fun layoutId(): Int = R.layout.activity_about

    override fun setupComponent() {
    }
}