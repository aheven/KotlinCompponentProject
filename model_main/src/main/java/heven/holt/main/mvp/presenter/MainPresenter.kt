package heven.holt.main.mvp.presenter

import com.blankj.utilcode.util.LogUtils
import heven.holt.lib_common.base.presenter.BasePresenter
import heven.holt.lib_common.http.RxObserver
import heven.holt.lib_common.model.HomeModel
import heven.holt.main.mvp.contract.MainContract

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {
    private val homeModel by lazy { mView?.let { HomeModel(it) } }
    override fun requestRecommonedList() {
        homeModel?.requestRecommonedRooms(RxObserver {
            LogUtils.i(it.toString())
        })
    }
}