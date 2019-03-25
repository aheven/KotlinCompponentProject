package heven.holt.main.api

import heven.holt.lib_common.model.vo.HttpRequest
import heven.holt.lib_common.model.vo.PagerHelperModel
import heven.holt.main.mvp.model.vo.RoomQuickVo
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiHomeService{
    //快速加入房间列表
    @POST("call.do?action=home.recommend")
    fun getRecommendRoomList(@Body httpRequest: HttpRequest): Observable<PagerHelperModel<RoomQuickVo>>
}