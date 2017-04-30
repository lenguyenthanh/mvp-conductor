package com.lenguyenthanh.mvp

import android.support.annotation.UiThread
import java.lang.ref.WeakReference

open class BasePresenter<V : MvpView> : Presenter<V> {
    private var viewRef: WeakReference<V>? = null

    @UiThread
    override fun attachView(view: V) {
        viewRef = WeakReference<V>(view)
    }

    val view get() = viewRef?.get()

    fun isViewAttached() = view != null

    @UiThread
    override fun detachView(retainInstance: Boolean) {
        viewRef?.clear()
        viewRef = null
    }
}