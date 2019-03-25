package heven.holt.lib_common.model

import heven.holt.lib_common.contract.BaseContract
import heven.holt.lib_common.http.RxObserver
import heven.holt.lib_common.model.vo.PagerHelperModel
import heven.holt.main.api.ApiHome
import heven.holt.main.mvp.model.vo.RoomQuickVo

class HomeModel(val mView: BaseContract.View) {
    fun requestRecommonedRooms(dataResult: RxObserver<PagerHelperModel<RoomQuickVo>>) {
        ApiHome.getRecommendRoomList(dataResult, mView.getLifecycleOwner(), 1)
    }
}