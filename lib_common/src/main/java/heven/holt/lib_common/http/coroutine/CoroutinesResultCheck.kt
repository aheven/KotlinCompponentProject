package heven.holt.lib_common.http.coroutine

import com.blankj.utilcode.util.ToastUtils
import heven.holt.lib_common.model.vo.SingleBaseModel

public fun <T : SingleBaseModel?> checkResult(t: T?): T? {
    if (t == null) {
        ToastUtils.showShort("请求值为null,网络请求失败")
        return null
    }
    if (!t.isSuccess) {
        ToastUtils.showShort(t.msg)
        return null
    }
    return t
}