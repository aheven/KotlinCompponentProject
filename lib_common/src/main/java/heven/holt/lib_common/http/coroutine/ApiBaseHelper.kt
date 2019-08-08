package heven.holt.lib_common.http.coroutine

import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.LogUtils
import heven.holt.lib_common.model.vo.SingleBaseModel
import kotlinx.coroutines.GlobalScope

interface ApiBaseHelper {
    fun <T : SingleBaseModel?> request(
        coroutinesResultCallback: CoroutinesResultCallback<T>,
        lifecycleOwner: LifecycleOwner,
        block: suspend () -> T?
    ) {
        GlobalScope.asyncWithLifecycle(lifecycleOwner) {
            try {
                block.invoke()
            } catch (ex: Exception) {
                LogUtils.e("http error : ${ex.message}")
                null
            }
        }.then {
            coroutinesResultCallback.forResult(it)
        }
    }
}