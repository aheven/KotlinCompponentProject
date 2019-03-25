package heven.holt.lib_common.base

import android.os.Bundle
import heven.holt.lib_common.contract.BaseContract

abstract class MvpBaseActivity<P : BaseContract.BasePresenter<*>> : BaseActivity() {
    protected lateinit var mPresenter: P

    override fun initActivity(savedInstanceState: Bundle?) {
        mPresenter = initPresenter()
        attachView()
        initActivityAfterPresenter(savedInstanceState)
    }

    abstract fun initPresenter(): P

    abstract fun initActivityAfterPresenter(savedInstanceState: Bundle?)

    @Suppress("UNCHECKED_CAST")
    private fun attachView() {
        mPresenter.attachView(this as BaseContract.View)
    }

    private fun detachView() {
        mPresenter.detachView()
    }

    override fun onDestroy() {
        detachView()
        super.onDestroy()
    }
}