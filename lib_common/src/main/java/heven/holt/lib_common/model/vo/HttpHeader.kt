package heven.holt.lib_common.model.vo

import android.os.Build
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.NetworkUtils

class HttpHeader {
    val clientModel: String = android.os.Build.MODEL//机型
    val network: String = NetworkUtils.getNetworkOperatorName()//网络类型
    val osVersion: String = Build.VERSION.RELEASE//系统OS版本
    val packager: String = AppUtils.getAppPackageName()//包名
    val version: String = AppUtils.getAppVersionCode().toString()
    val clientType = "0"//客户端类型 0 安卓，1 iOS
}