package heven.holt.lib_common.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.Utils
import heven.holt.lib_common.utils.xselector.XSelector

open class BaseApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        XSelector.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // dex突破65535的限制
        MultiDex.install(this)
    }
}