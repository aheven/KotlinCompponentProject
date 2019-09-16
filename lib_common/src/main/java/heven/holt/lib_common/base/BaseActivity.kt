package heven.holt.lib_common.base

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import heven.holt.lib_common.contract.BaseContract

abstract class BaseActivity : AppCompatActivity(), BaseContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStatusBar()
        setContentView(getLayoutResID())
        initActivity(savedInstanceState)
    }

    private fun initStatusBar() {
        BarUtils.setStatusBarLightMode(this, true)
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
    }

    abstract fun getLayoutResID(): Int

    abstract fun initActivity(savedInstanceState: Bundle?)

    override fun showMsgToast(msg: String) {
        ToastUtils.showShort(msg)
    }

    override fun showMsgToast(resId: Int) {
        ToastUtils.showShort(resId)
    }

    override fun getLifecycleOwner() = this

    override fun getResources(): Resources =
        AdaptScreenUtils.adaptHeight(super.getResources(), 1920)
}