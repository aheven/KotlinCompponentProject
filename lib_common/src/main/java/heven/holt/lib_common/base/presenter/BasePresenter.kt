package heven.holt.lib_common.base.presenter

import heven.holt.lib_common.contract.BaseContract

open class BasePresenter<T : BaseContract.View> : BaseContract.BasePresenter<T> {
    protected var mView: T? = null

    @Suppress("UNCHECKED_CAST")
    override fun attachView(view: BaseContract.View) {
        this.mView = view as T
    }

    override fun detachView() {
        mView = null
    }
}