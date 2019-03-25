package debug

import com.blankj.utilcode.util.LogUtils
import heven.holt.lib_common.base.BaseApplication

class MainApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        LogUtils.i("main application init")
    }
}