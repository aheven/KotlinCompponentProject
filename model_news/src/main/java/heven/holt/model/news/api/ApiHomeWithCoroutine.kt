package heven.holt.model.news.api

import androidx.lifecycle.LifecycleOwner
import heven.holt.lib_common.http.coroutine.ApiBaseHelper
import heven.holt.lib_common.http.coroutine.ApiServiceManagerWithCoroutine
import heven.holt.lib_common.http.coroutine.CoroutinesResultCallback
import heven.holt.lib_common.model.vo.HttpEntry
import heven.holt.lib_common.model.vo.HttpRequest
import heven.holt.lib_common.model.vo.PagerHelperModel
import heven.holt.model.news.mvp.model.vo.RoomQuickVo
import kotlinx.coroutines.delay

object ApiHomeWithCoroutine : ApiBaseHelper {
    private val sService = ApiServiceManagerWithCoroutine.create(ApiHomeWithCoroutineService::class.java)
    /**
     * 获取首页推荐列表
     */
    fun getRecommendRoomList(
        coroutinesResultCallback: CoroutinesResultCallback<PagerHelperModel<RoomQuickVo>?>,
        lifecycleOwner: LifecycleOwner,
        pageNo: Int
    ) {
        request(coroutinesResultCallback, lifecycleOwner) {
            delay(3000)
            sService
                .getRecommendRoomList(
                    HttpRequest.obtainHttpRequest(
                        HttpEntry("pageNo", pageNo),
                        HttpEntry("pageSize", 10)
                    )
                )
        }
    }
}