package heven.holt.main.ui

import android.os.Bundle
import android.view.View
import heven.holt.lib_common.base.MvpBaseActivity
import heven.holt.main.mvp.contract.MainContract
import heven.holt.main.mvp.presenter.MainPresenter
import heven.holt.model_main.R
import kotlinx.android.synthetic.main.main_activity_main.*

class MainActivity : MvpBaseActivity<MainPresenter>(), MainContract.View {

    override fun initPresenter(): MainPresenter = MainPresenter()

    override fun getLayoutResID(): Int = R.layout.main_activity_main

    override fun initActivityAfterPresenter(savedInstanceState: Bundle?) {
        text.text = "切换前的文字"
    }

    fun clickButton(view: View) {
        when (view.id) {
            R.id.text -> mPresenter.requestRecommonedList()
        }
    }
}