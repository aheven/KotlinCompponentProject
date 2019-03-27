package heven.holt.model.news.mvp.model

import heven.holt.lib_common.contract.BaseContract
import heven.holt.lib_common.http.RxObserver
import heven.holt.lib_common.model.vo.PagerHelperModel
import heven.holt.model.news.api.ApiHome
import heven.holt.model.news.mvp.model.vo.RoomQuickVo

class HomeModel(val mView: BaseContract.View) {
    fun requestRecommonedRooms(dataResult: RxObserver<PagerHelperModel<RoomQuickVo>>) {
        ApiHome.getRecommendRoomList(dataResult, mView.getLifecycleOwner(), 1)
    }
}