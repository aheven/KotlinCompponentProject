package heven.holt.lib_common.base.mvp

import android.os.Bundle
import heven.holt.lib_common.base.BaseFragment
import heven.holt.lib_common.contract.BaseContract

abstract class MvpBaseFragment<P : BaseContract.BasePresenter<*>>: BaseFragment(){
    protected lateinit var mPresenter: P

    override fun initFragment(savedInstanceState: Bundle?) {
        mPresenter = initPresenter()
        attachView()
        initFragmentAfterPresenter(savedInstanceState)
    }

    abstract fun initPresenter(): P

    abstract fun initFragmentAfterPresenter(savedInstanceState: Bundle?)

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