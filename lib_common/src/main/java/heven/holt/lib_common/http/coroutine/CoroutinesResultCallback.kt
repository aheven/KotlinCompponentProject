package heven.holt.lib_common.http.coroutine

import heven.holt.lib_common.model.vo.SingleBaseModel

class CoroutinesResultCallback<T : SingleBaseModel?>(val doNext: (T?) -> Unit) {
    fun forResult(t: T?) {
        doNext.invoke(checkResult(t))
    }
}