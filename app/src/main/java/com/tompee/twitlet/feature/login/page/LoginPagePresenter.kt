package com.tompee.twitlet.feature.login.page

import android.util.Patterns
import com.tompee.twitlet.Constants.MIN_PASS_COUNT
import com.tompee.twitlet.base.BasePresenter
import com.tompee.twitlet.interactor.AuthInteractor
import com.tompee.twitlet.interactor.DataInteractor
import com.tompee.twitlet.model.User
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.withLatestFrom
import java.util.concurrent.TimeUnit

class LoginPagePresenter(private val authInteractor: AuthInteractor,
                         private val dataInteractor: DataInteractor,
                         private val user: User,
                         private val io: Scheduler,
                         private val ui: Scheduler) : BasePresenter<LoginPageView>() {
    enum class InputError {
        EMAIL_EMPTY,
        EMAIL_INVALID,
        EMAIL_OK,
        PASSWORD_EMPTY,
        PASSWORD_SHORT,
        PASSWORD_OK,
        BOTH_OK
    }

    override fun onViewAttached(view: LoginPageView) {
        val inputError = validateInputs()
        setupCommandHandler(inputError)
    }

    override fun onViewDetached() {
    }

    private fun validateInputs(): Observable<InputError> {
        val emailError = view.getEmail()
                .map {
                    when {
                        it.isEmpty() -> InputError.EMAIL_EMPTY
                        !Patterns.EMAIL_ADDRESS.matcher(it).matches() -> InputError.EMAIL_INVALID
                        else -> InputError.EMAIL_OK
                    }
                }
        val passError = view.getPassword()
                .map {
                    when {
                        it.isEmpty() -> InputError.PASSWORD_EMPTY
                        it.length < MIN_PASS_COUNT -> InputError.PASSWORD_SHORT
                        else -> InputError.PASSWORD_OK
                    }
                }

        addSubscription(emailError
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(ui)
                .subscribe(this::showError))
        addSubscription(passError
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(ui)
                .subscribe(this::showError))

        return Observables.combineLatest(
                Observable.just(InputError.EMAIL_EMPTY).concatWith(emailError),
                Observable.just(InputError.PASSWORD_EMPTY).concatWith(passError)) { email, pass ->
            when {
                email != InputError.EMAIL_OK -> email
                pass != InputError.PASSWORD_OK -> pass
                else -> InputError.BOTH_OK
            }
        }
    }

    private fun showError(error: InputError) = when (error) {
        InputError.EMAIL_EMPTY -> view.showEmptyEmailError()
        InputError.EMAIL_INVALID -> view.showInvalidEmailError()
        InputError.EMAIL_OK -> view.clearEmailError()
        InputError.PASSWORD_EMPTY -> view.showEmptyPasswordError()
        InputError.PASSWORD_SHORT -> view.showPasswordShortError()
        InputError.PASSWORD_OK -> view.clearEmailError()
        else -> {
            view.clearEmailError()
            view.clearPasswordError()
        }
    }

    private fun setupCommandHandler(inputError: Observable<InputError>) {
        val command = view.command().withLatestFrom(inputError) { _, error -> error }
                .observeOn(ui)
                .doOnNext(this::showError)
                .filter { it == InputError.BOTH_OK }
                .doOnNext { view.showProgressDialog() }
                .withLatestFrom(view.getEmail(), view.getPassword()) { _, email, pass -> Pair(email, pass) }
        if (view.getViewType() == LoginFragment.SIGN_UP) {
            addSubscription(command.flatMapCompletable { pair ->
                authInteractor.signup(pair.first, pair.second)
                        .observeOn(ui)
                        .doOnComplete(view::showSignupSuccessMessage)
                        .doOnError { view.showError(it.message ?: "Error occurred") }
                        .onErrorComplete()
                        .doOnComplete(view::dismissProgressDialog)
                        .subscribeOn(io)
            }.subscribe())
        } else {
            addSubscription(command.flatMapSingle { pair ->
                authInteractor.login(pair.first, pair.second)
                        .observeOn(ui)
                        .doOnError {
                            view.showError(it.message ?: "Error occurred")
                            view.dismissProgressDialog()
                        }
                        .doOnSuccess {
                            user.email = it
                            user.isAuthenticated = true
                        }
                        .flatMap { email ->
                            dataInteractor.getUser(email)
                                    .observeOn(ui)
                                    .doOnSuccess {
                                        user.nickname = it.nickname
                                        user.image = it.image
                                        view.moveToTimelineScreen()
                                    }
                                    .doOnError { view.moveToProfileScreen() }
                                    .onErrorResumeNext(Single.just(User()))
                                    .subscribeOn(io)
                        }
                        .subscribeOn(io)
            }.subscribe())
        }
    }
}