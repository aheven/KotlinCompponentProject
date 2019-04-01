package heven.holt.model.news

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import heven.holt.lib_common.base.BaseActivity

@Route(path = "/news/main")
class NewActivity : BaseActivity() {
    override fun getLayoutResID(): Int = R.layout.news_activity_news

    override fun initActivity(savedInstanceState: Bundle?) {
    }
}