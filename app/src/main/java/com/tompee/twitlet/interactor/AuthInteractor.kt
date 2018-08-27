package com.tompee.twitlet.interactor

import com.tompee.twitlet.core.auth.Authenticator

class AuthInteractor(private val authenticator: Authenticator) {

    fun signup(email: String, pass: String) = authenticator.signup(email, pass)

    fun login(email: String, pass: String) = authenticator.login(email, pass)
}