package com.tompee.twitlet.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<T : BaseView, V : BaseInteractor>(protected val interactor: V) {

    private val subscriptions = CompositeDisposable()

    protected lateinit var view: T

    fun attachView(view: T) {
        this.view = view
        onViewAttached(view)
    }

    fun detachView() {
        subscriptions.clear()
        onViewDetached()
    }

    abstract fun onViewAttached(view: T)

    abstract fun onViewDetached()

    protected fun addSubscription(subscription: Disposable) {
        subscriptions.add(subscription)
    }
}