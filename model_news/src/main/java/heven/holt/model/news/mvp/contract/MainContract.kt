package heven.holt.model.news.mvp.contract

import heven.holt.lib_common.contract.BaseContract
import heven.holt.model.news.mvp.model.vo.RoomQuickVo

interface MainContract {
    interface Presenter : BaseContract.BasePresenter<View> {
        fun requestRecommendList()
    }

    interface View : BaseContract.View {
        fun requestRecommendListSuccess(data: MutableList<RoomQuickVo>?)
    }
}