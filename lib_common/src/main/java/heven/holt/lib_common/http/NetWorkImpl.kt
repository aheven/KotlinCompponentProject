package heven.holt.lib_common.http

import android.os.Build
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils

object NetWorkImpl {
    fun getUserAgent(): String {
        return try {
            String.format(
                "%s/%s (Linux; Android %s; %s Build/%s)", Utils.getApp().packageName
                , AppUtils.getAppVersionCode(),
                Build.VERSION.RELEASE, Build.MANUFACTURER, Build.ID
            )
        } catch (e: Exception) {
            "Android"
        }
    }
}