package heven.holt.main.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import heven.holt.lib_common.base.BaseActivity
import heven.holt.model_main.R
import kotlinx.android.synthetic.main.main_activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutResID(): Int = R.layout.main_activity_main

    override fun initActivity(savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        initStartTabLayout()
    }

    private fun initStartTabLayout() {
        val titles = arrayOf("news1", "news2", "news3")
        val fragments = FragmentPagerItems(this)
        titles.forEach {
            val newsFragment = ARouter.getInstance().build("/news/fragment").navigation()
            fragments.add(FragmentPagerItem.of(it, newsFragment::class.java as Class<out Fragment>?))
        }
        val adapter = FragmentPagerItemAdapter(supportFragmentManager, fragments)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2
        smartTabLayout.setViewPager(viewPager)
    }
}