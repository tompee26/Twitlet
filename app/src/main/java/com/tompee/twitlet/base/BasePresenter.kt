package com.tompee.twitlet.base

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : BaseView> {

    private val subscription = CompositeDisposable()

    protected var view: T? = null
        private set

    fun attachView(view: T) {
        this.view = view
        onViewAttached()
    }

    fun detachView() {
        subscription.clear()
        onViewDetached()
        view = null
    }

    abstract fun onViewAttached()

    abstract fun onViewDetached()
}