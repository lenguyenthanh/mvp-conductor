package com.lenguyenthanh.mvp

import android.os.Bundle
import com.bluelinelabs.conductor.Controller

abstract class MvpController<V : MvpView, P : Presenter<V>> : Controller, PresenterFactory<P> {

    init {
        val callback = object : MvpDelegateCallback<V, P> {
            override fun createPresenter(): P = createPresenter()

            override fun getPresenter(): P? = _presenter

            override fun setPresenter(presenter: P) {
                _presenter = presenter
            }

            @Suppress("UNCHECKED_CAST")
            override fun getMvpView(): V = this as V
        }
        this.addLifecycleListener(MvpLifecycleListener(callback))
    }

    constructor() : super()
    constructor(bundle: Bundle?) : super(bundle)

    private lateinit var _presenter: P

    val presenter get() = _presenter
}