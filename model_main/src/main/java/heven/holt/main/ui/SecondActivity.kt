package heven.holt.main.ui

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import heven.holt.lib_common.base.BaseActivity
import heven.holt.model_main.R
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SecondActivity : BaseActivity() {
    override fun getLayoutResID(): Int = R.layout.main_activity_second

    override fun initActivity(savedInstanceState: Bundle?) {
        Observable.interval(1, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe { LogUtils.e("rxjava runing") }
    }
}