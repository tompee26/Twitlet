package com.tompee.twitlet.feature.splash

import android.content.Intent
import android.os.Bundle
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseActivity
import com.tompee.twitlet.dependency.component.DaggerSplashComponent
import com.tompee.twitlet.dependency.module.SplashModule
import com.tompee.twitlet.feature.login.LoginActivity
import com.tompee.twitlet.feature.profile.ProfileActivity
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashView {

    @Inject
    lateinit var splashPresenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashPresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        splashPresenter.detachView()
    }

    override fun layoutId(): Int = R.layout.activity_splash

    override fun setupComponent() {
        DaggerSplashComponent.builder()
                .appComponent(TwitletApplication[this].component)
                .splashModule(SplashModule())
                .build()
                .inject(this)
    }

    //region SplashView
    override fun moveToProfileScreen() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun moveToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    //endregion
}