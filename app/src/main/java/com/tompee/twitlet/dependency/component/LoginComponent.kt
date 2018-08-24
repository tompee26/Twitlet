package com.tompee.twitlet.dependency.component

import com.tompee.twitlet.dependency.module.LoginModule
import com.tompee.twitlet.dependency.scope.LoginScope
import com.tompee.twitlet.feature.login.LoginActivity
import com.tompee.twitlet.feature.login.page.LoginFragment
import dagger.Component

@LoginScope
@Component(dependencies = [AppComponent::class], modules = [LoginModule::class])
interface LoginComponent {

    fun inject(loginActivity: LoginActivity)

    fun inject(loginFragment: LoginFragment)
}