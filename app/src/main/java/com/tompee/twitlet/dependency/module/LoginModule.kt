package com.tompee.twitlet.dependency.module

import android.support.v4.app.FragmentActivity
import com.tompee.twitlet.dependency.scope.LoginScope
import com.tompee.twitlet.feature.login.LoginPageAdapter
import com.tompee.twitlet.feature.login.page.LoginFragment
import com.tompee.twitlet.feature.login.page.LoginPagePresenter
import com.tompee.twitlet.interactor.AuthInteractor
import com.tompee.twitlet.interactor.DataInteractor
import com.tompee.twitlet.model.User
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
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
    fun provideLoginPagePresenter(authInteractor: AuthInteractor,
                                  dataInteractor: DataInteractor,
                                  user: User,
                                  @Named("io") io: Scheduler,
                                  @Named("ui") ui: Scheduler) =
            LoginPagePresenter(authInteractor, dataInteractor, user, io, ui)
}