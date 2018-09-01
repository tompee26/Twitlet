package com.tompee.twitlet.dependency.module

import android.support.v4.app.FragmentActivity
import com.tompee.twitlet.base.Schedulers
import com.tompee.twitlet.core.auth.Authenticator
import com.tompee.twitlet.core.database.UserDao
import com.tompee.twitlet.dependency.scope.LoginScope
import com.tompee.twitlet.feature.login.LoginPageAdapter
import com.tompee.twitlet.feature.login.page.LoginFragment
import com.tompee.twitlet.feature.login.page.LoginPagePresenter
import com.tompee.twitlet.interactor.LoginInteractor
import com.tompee.twitlet.model.User
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class LoginModule(private val fragmentActivity: FragmentActivity) {

    @LoginScope
    @Provides
    @Named("login")
    fun provideLoginFragment() = LoginFragment.newInstance(LoginFragment.LOGIN)

    @LoginScope
    @Provides
    @Named("signup")
    fun provideSignupFragment() = LoginFragment.newInstance(LoginFragment.SIGN_UP)

    @LoginScope
    @Provides
    fun provideLoginPageAdapter(@Named("login") loginFragment: LoginFragment,
                                @Named("signup") signupFragment: LoginFragment) =
            LoginPageAdapter(fragmentActivity.supportFragmentManager, loginFragment, signupFragment)

    @Provides
    fun provideLoginPagePresenter(loginInteractor: LoginInteractor,
                                  schedulers: Schedulers) =
            LoginPagePresenter(loginInteractor, schedulers)

    @LoginScope
    @Provides
    fun provideLoginInteractor(authenticator: Authenticator,
                               userDao: UserDao,
                               user: User): LoginInteractor =
            LoginInteractor(authenticator, userDao, user)

}