package com.lenguyenthanh.mvp

import android.app.Activity
import android.view.View
import com.bluelinelabs.conductor.Controller
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MvpLifecycleListenerTest {

    private val view = mock<View>()
    private val callback = mock<MvpDelegateCallback<MvpView, Presenter<MvpView>>>()
    private val controller = mock<Controller>()
    private val presenter = mock<Presenter<MvpView>>()
    private val mvpView = mock<MvpView>()
    private val activity = mock<Activity>()

    private lateinit var mvpLifecycleListener: MvpLifecycleListener<MvpView, Presenter<MvpView>>

    @BeforeEach
    fun beforeEach() {
        mvpLifecycleListener = MvpLifecycleListener(callback)
    }

    @Test
    @DisplayName("postCreateView should call callback.createPresenter() when callback.getPresenter() is null")
    fun postCreateView_callback_getPresenter_isNull() {
        whenever(callback.getPresenter()).thenReturn(null)
        whenever(callback.createPresenter()).thenReturn(presenter)
        whenever(callback.getMvpView()).thenReturn(mvpView)

        mvpLifecycleListener.postCreateView(controller, view)

        verify(callback, times(1)).getPresenter()
        verify(callback, times(1)).createPresenter()
        verify(callback, times(1)).setPresenter(presenter)
        verify(callback, times(1)).getMvpView()
        verify(presenter, times(1)).attachView(mvpView)
        verifyNoMoreInteractions(callback)
    }

    @Test
    @DisplayName("postCreateView should not call callback.createPresenter() when callback.getPresenter() is not null")
    fun postCreateView_callback_getPresenter_exist() {
        whenever(callback.getPresenter()) doReturn presenter
        whenever(callback.getMvpView()) doReturn mvpView

        mvpLifecycleListener.postCreateView(controller, view)

        verify(callback, times(1)).getPresenter()
        verify(callback, never()).createPresenter()
        verify(callback, never()).setPresenter(presenter)
        verify(callback, times(1)).getMvpView()
        verify(presenter, times(1)).attachView(mvpView)
        verifyNoMoreInteractions(callback)
    }

    @Test
    @DisplayName("preDestroyView should throw NullPointerException when callback.getPresenter() is null")
    fun preDestroyView_getPresenter_isNull() {
        try {
            whenever(callback.getPresenter()).thenReturn(null)
            mvpLifecycleListener.preDestroyView(controller, view)
        } catch (ex: NullPointerException) {
            assertTrue(true)
        }
    }

    @Test
    @DisplayName("preDestroyView should call presenter.detachView(true) when callback.getPresenter() is not null and activity.isChangingConfigurations is True")
    fun preDestroyView_getPresenter_exist_isChangingConfigurations() {
        whenever(callback.getPresenter()).thenReturn(presenter)
        whenever(activity.isChangingConfigurations).thenReturn(true)
        whenever(controller.activity).thenReturn(activity)

        mvpLifecycleListener.preDestroyView(controller, view)

        verify(presenter, times(1)).detachView(true)
    }

    @Test
    @DisplayName("preDestroyView should call presenter.detachView(false) when callback.getPresenter() is not null and activity.isChangingConfigurations is False")
    fun preDestroyView_getPresenter_exist_isNotChangingConfigurations() {
        whenever(callback.getPresenter()).thenReturn(presenter)
        whenever(activity.isChangingConfigurations).thenReturn(false)
        whenever(controller.activity).thenReturn(activity)

        mvpLifecycleListener.preDestroyView(controller, view)

        verify(presenter, times(1)).detachView(false)
    }
}