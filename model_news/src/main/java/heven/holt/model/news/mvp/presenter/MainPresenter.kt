package heven.holt.model.news.mvp.presenter

import heven.holt.lib_common.base.presenter.BasePresenter
import heven.holt.lib_common.http.RxObserver
import heven.holt.model.news.mvp.model.HomeModel
import heven.holt.model.news.mvp.contract.MainContract

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {
    private val homeModel by lazy { mView?.let { HomeModel(it) } }
    override fun requestRecommendList() {
        homeModel?.requestRecommonedRooms(RxObserver {
            mView?.requestRecommendListSuccess(it.data)
        })
    }
}