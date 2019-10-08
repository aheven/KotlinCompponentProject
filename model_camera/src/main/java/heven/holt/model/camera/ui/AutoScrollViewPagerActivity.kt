package heven.holt.model.camera.ui

import android.os.Bundle
import heven.holt.lib_common.base.BaseActivity
import heven.holt.model.camera.R
import kotlinx.android.synthetic.main.camera_auto_scroll_view_pager.*

/**
 *Time:2019/9/30
 *Author:HevenHolt
 *Description:自动无限循环的viewpager
 */
class AutoScrollViewPagerActivity : BaseActivity() {
    override fun getLayoutResID(): Int = R.layout.camera_auto_scroll_view_pager

    override fun initActivity(savedInstanceState: Bundle?) {
        val data = listOf(
            "https://gss3.bdstatic.com/7Po3dSag_xI4khGkpoWK1HF6hhy/baike/s%3D220/sign=ecdeab0348a98226bcc12c25ba82b97a/622762d0f703918fa7f614bc5a3d269759eec4a4.jpg",
            "https://pic1.zhimg.com/v2-6059d4d83dd2899803ecdd0a4f6b7db7_1200x500.jpg",
            "https://p.nanrenwo.net/uploads/allimg/190724/8517-1ZH4113942.png",
            "https://p.nanrenwo.net/uploads/allimg/181206/8500-1Q206114508.png"
        )
        auto_scroll_view.setImageUrls(data)
//        val viewPager = auto_scroll_view.getViewPager()
//        viewPager.setPageTransformer(true, GraceRotationPageTransformer(viewPager.adapter as GracePagerAdapter<*>))
//        val mMultiPagePlugin = GraceMultiPagePlugin.Builder(viewPager)
//            .pageHorizontalMinMargin(ConvertUtils.dp2px(35f))
//            .build()
//        GraceViewPagerSupport.supportMultiPage(viewPager, mMultiPagePlugin)
    }
}