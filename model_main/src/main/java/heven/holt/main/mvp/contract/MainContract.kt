package heven.holt.main.mvp.contract

import heven.holt.lib_common.contract.BaseContract

interface MainContract {
    interface Presenter : BaseContract.BasePresenter<View> {
        fun requestRecommonedList()
    }

    interface View : BaseContract.View {
    }
}