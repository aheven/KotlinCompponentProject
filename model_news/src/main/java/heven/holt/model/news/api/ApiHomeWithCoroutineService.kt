package heven.holt.model.news.api

import heven.holt.lib_common.model.vo.HttpRequest
import heven.holt.lib_common.model.vo.PagerHelperModel
import heven.holt.model.news.mvp.model.vo.RoomQuickVo
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiHomeWithCoroutineService {
    //快速加入房间列表
    @POST("call.do?action=home.listQuickRoom")
    suspend fun getRecommendRoomList(@Body httpRequest: HttpRequest): PagerHelperModel<RoomQuickVo>
}