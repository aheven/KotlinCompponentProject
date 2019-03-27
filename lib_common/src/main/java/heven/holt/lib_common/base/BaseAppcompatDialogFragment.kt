package heven.holt.lib_common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.blankj.utilcode.util.ToastUtils
import heven.holt.lib_common.contract.BaseContract

abstract class BaseAppcompatDialogFragment : AppCompatDialogFragment(), BaseContract.View{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResID(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFragment(savedInstanceState)
    }

    abstract fun getLayoutResID(): Int

    abstract fun initFragment(savedInstanceState: Bundle?)

    override fun showMsgToast(msg: String) {
        ToastUtils.showShort(msg)
    }

    override fun showMsgToast(resId: Int) {
        ToastUtils.showShort(resId)
    }

    override fun getLifecycleOwner() = this
}