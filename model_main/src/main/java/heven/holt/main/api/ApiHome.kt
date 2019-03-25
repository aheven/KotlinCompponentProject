package heven.holt.main.api

import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import heven.holt.lib_common.http.ApiServiceManager
import heven.holt.lib_common.http.RxHelper
import heven.holt.lib_common.http.RxObserver
import heven.holt.lib_common.model.vo.HttpEntry
import heven.holt.lib_common.model.vo.HttpRequest
import heven.holt.lib_common.model.vo.PagerHelperModel
import heven.holt.main.mvp.model.vo.RoomQuickVo

object ApiHome {
    private val sService = ApiServiceManager.create(ApiHomeService::class.java)

    /**
     * 获取首页推荐列表
     */
    fun getRecommendRoomList(
        rxObserver: RxObserver<PagerHelperModel<RoomQuickVo>>,
        lifecycleOwner: LifecycleOwner,
        pageNo: Int
    ) {
        sService.getRecommendRoomList(
            HttpRequest.obtainHttpRequest(
                HttpEntry("pageNo", pageNo),
                HttpEntry("pageSize", 10)
            )
        )
            .compose(RxHelper.handleSingleResult())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
            .subscribe(rxObserver)
    }
}