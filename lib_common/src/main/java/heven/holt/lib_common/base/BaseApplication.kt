package heven.holt.lib_common.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.Utils

open class BaseApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // dex突破65535的限制
        MultiDex.install(this)
    }
}