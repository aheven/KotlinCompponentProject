package heven.holt.main.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
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
        val titles = arrayOf("news1", "camera")
        val fragments = FragmentPagerItems(this)
        val newsFragment = ARouter.getInstance().build("/news/fragment").navigation() as Fragment
        val cameraFragment = ARouter.getInstance().build("/camera/fragment").navigation() as Fragment
        fragments.add(FragmentPagerItem.of("news1", newsFragment::class.java))
        fragments.add(FragmentPagerItem.of("camera", cameraFragment::class.java))
        smartTabLayout.setCustomTabView { container, position, _ ->
            val rootView = layoutInflater.inflate(R.layout.main_smart_layout_icon_text, container, false)
            val icon = rootView.findViewById<ImageView>(R.id.custom_tab_icon)
            val text = rootView.findViewById<TextView>(R.id.custom_tab_text)
            when (position) {
                0 -> icon.setImageResource(R.drawable.main_custom_tab_image)
                1 -> icon.setImageResource(R.drawable.main_custom_tab_image1)
                2 -> icon.setImageResource(R.drawable.main_custom_tab_image)
            }
            text.text = titles[position]
            rootView
        }
        val adapter = FragmentPagerItemAdapter(supportFragmentManager, fragments)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2
        smartTabLayout.setViewPager(viewPager)
    }
}