package com.lenguyenthanh.mvp

import android.support.annotation.UiThread
import android.view.View
import com.bluelinelabs.conductor.Controller

interface MvpView

interface Presenter<in V : MvpView> {
    @UiThread
    fun attachView(view: V)

    @UiThread
    fun detachView(retainInstance: Boolean)
}

class MvpLifecycleListener<V : MvpView, P : Presenter<V>>(val callback: MvpDelegateCallback<V, P>) : Controller.LifecycleListener() {

    override fun postCreateView(controller: Controller, view: View) {
        var presenter = callback.getPresenter()
        if (presenter == null) {
            presenter = callback.createPresenter()
            callback.setPresenter(presenter)
        }

        val mvpView = callback.getMvpView()
        presenter.attachView(mvpView)
    }

    override fun preDestroyView(controller: Controller, view: View) {
        val presenter = callback.getPresenter() ?: throw NullPointerException(
                "Presenter returned from getPresenter() is null in " + callback)
        presenter.detachView(controller.activity?.isChangingConfigurations ?: false)
    }
}

interface PresenterFactory<out P> {
    fun createPresenter(): P
}

interface MvpDelegateCallback<V : MvpView, P : Presenter<V>> : PresenterFactory<P> {
    fun getPresenter(): P?
    fun setPresenter(presenter: P)
    fun getMvpView(): V
}

//class HomeController : MvpController<HomeView, HomePresenter>(), HomeView {
//
//    constructor() : super()
//    constructor(bundle: Bundle?) : super(bundle)
//
//    @Inject lateinit var navigator: Navigator
//    private lateinit var component: HomeComponent
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
//        initDaggerComponent().inject(this@HomeController)
//        val view = container.inflate(inflater, R.layout.activity_main)
//        onViewBound(view)
//        return view
//    }
//
//    private fun initDaggerComponent() = DaggerHomeComponent.builder()
//            .activityComponent(mainActivity().activityComponent)
//            .homeModule(HomeModule(this@HomeController))
//            .build()
//
//    override fun createPresenter() = component.homePresenter()
//
//    private fun onViewBound(view: View) {
//        presenter.bind(this)
//    }
//
//    override fun render(model: HomeState) {
//
//    }
//}