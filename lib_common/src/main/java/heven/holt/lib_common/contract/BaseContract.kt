package heven.holt.lib_common.contract

import androidx.lifecycle.LifecycleOwner

interface BaseContract {
    interface BasePresenter<T : View> {
        fun attachView(view: View)

        fun detachView()
    }

    interface View {
        fun showMsgToast(msg: String)

        fun showMsgToast(resId: Int)

        fun getLifecycleOwner(): LifecycleOwner
    }
}