package com.tompee.twitlet.feature.login

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import com.tompee.twitlet.R
import com.tompee.twitlet.TwitletApplication
import com.tompee.twitlet.base.BaseActivity
import com.tompee.twitlet.dependency.component.DaggerLoginComponent
import com.tompee.twitlet.dependency.component.LoginComponent
import com.tompee.twitlet.dependency.module.LoginModule
import com.tompee.twitlet.feature.login.page.LoginFragment
import com.tompee.twitlet.feature.login.page.PageSwitchListener
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity(), ViewPager.PageTransformer, ViewPager.OnPageChangeListener,
        PageSwitchListener {
    @Inject
    lateinit var loginPageAdapter: LoginPageAdapter

    lateinit var loginComponent: LoginComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewpager.addOnPageChangeListener(this)
        viewpager.setPageTransformer(false, this)
        viewpager.adapter = loginPageAdapter
    }

    override fun layoutId() = R.layout.activity_login

    override fun setupComponent() {
        loginComponent = DaggerLoginComponent.builder()
                .appComponent(TwitletApplication[this].component)
                .loginModule(LoginModule(this))
                .build()
        loginComponent.inject(this)
    }

    //region PageTransformer
    override fun transformPage(view: View, position: Float) {
        val translation = view.width * position
        when {
            position < -1 -> view.alpha = 0f
            position <= 1 -> {
                view.findViewById<View>(R.id.tv_app_name).translationX = -translation
                view.findViewById<View>(R.id.tv_app_subtitle).translationX = -translation

                view.findViewById<View>(R.id.userView).translationX = translation
                view.findViewById<View>(R.id.tv_user_label).translationX = translation
                view.findViewById<View>(R.id.view_user_underline).translationX = translation
                view.findViewById<View>(R.id.profileImage).translationX = translation

                view.findViewById<View>(R.id.passView).translationX = translation
                view.findViewById<View>(R.id.tv_pass_label).translationX = translation
                view.findViewById<View>(R.id.view_pass_underline).translationX = translation
                view.findViewById<View>(R.id.iv_pass_icon).translationX = translation

                view.findViewById<View>(R.id.commandButton).translationX = -translation
            }
            else -> view.alpha = 0f
        }
    }
    //endregion

    //region PageChangeListener
    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val x = ((viewpager.width * position + positionOffsetPixels) * computeFactor())
        scrollView.scrollTo(x.toInt() + imageView.width / 3, 0)
    }

    override fun onPageSelected(position: Int) {
    }

    private fun computeFactor(): Float {
        return (imageView.width / 2 - viewpager.width) / (viewpager.width *
                loginPageAdapter.count - 1).toFloat()
    }
    //endregion

    //region PageSwitchListener
    override fun onPageSwitch(type: Int) {
        if (type == LoginFragment.LOGIN) {
            viewpager.currentItem = 1
        } else {
            viewpager.currentItem = 0
        }
    }
    //endregion
}
