package heven.holt.model.news.mvp.presenter

import heven.holt.lib_common.base.presenter.BasePresenter
import heven.holt.lib_common.http.coroutine.CoroutinesResultCallback
import heven.holt.model.news.mvp.contract.MainContract
import heven.holt.model.news.mvp.model.HomeModel

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {
    private val homeModel by lazy { mView?.let { HomeModel(it) } }
    override fun requestRecommendList() {
//        homeModel?.requestRecommonedRooms(RxObserver {
//            mView?.requestRecommendListSuccess(it.data)
//        })
        homeModel?.requestRecommonedRoomsByCoroutine(CoroutinesResultCallback {
            if (it != null) {
                mView?.requestRecommendListSuccess(it.data)
            } else {
                mView?.requestRecommendListFailed()
            }
        }, mView!!.getLifecycleOwner())

    }
}