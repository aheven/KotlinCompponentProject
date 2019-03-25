package heven.holt.lib_common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import heven.holt.lib_common.contract.BaseContract

abstract class BaseActivity : AppCompatActivity(),BaseContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResID())
        initActivity(savedInstanceState)
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
}