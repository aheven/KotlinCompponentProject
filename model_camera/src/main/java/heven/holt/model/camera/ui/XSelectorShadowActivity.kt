package heven.holt.model.camera.ui

import android.graphics.Color
import android.os.Bundle
import heven.holt.lib_common.base.BaseActivity
import heven.holt.lib_common.utils.xselector.XSelector
import heven.holt.lib_common.utils.xselector.shadow.ShadowHelper
import heven.holt.model.camera.R
import kotlinx.android.synthetic.main.camera_activity_shadow.*

class XSelectorShadowActivity : BaseActivity() {
    override fun getLayoutResID(): Int = R.layout.camera_activity_shadow

    override fun initActivity(savedInstanceState: Bundle?) {
        initShadowDrawable()
    }

    private fun initShadowDrawable() {
        XSelector.shadowHelper()
            .setBgColor(Color.parseColor("#3D5AFE"))
            .setShapeRadius(8f)
            .setShadowColor(Color.parseColor("#66000000"))
            .setShadowRadius(20f)
            .into(text1)

        XSelector.shadowHelper().setBgColor(Color.parseColor("#2979FF"))
            .setShapeRadius(8f)
            .setShadowColor(Color.parseColor("#992979FF"))
            .setShadowRadius(6f)
            .setOffsetY(4f)
            .into(text2)

        XSelector.shadowHelper().setBgColor(intArrayOf(Color.parseColor("#536DFE"), Color.parseColor("#7C4DFF")))
            .setShapeRadius(8f)
            .setShadowColor(Color.parseColor("#997C4DFF"))
            .setShadowRadius(5f)
            .setOffsetX(5f)
            .setOffsetY(5f)
            .into(text3)

        XSelector.shadowHelper().setShape(ShadowHelper.SHAPE_CIRCLE)
            .setBgColor(Color.parseColor("#1DE9B6"))
            .setShadowColor(Color.parseColor("#99FF3D00"))
            .setShadowRadius(8f)
            .into(text4)

        XSelector.shadowHelper().setShape(ShadowHelper.SHAPE_CIRCLE)
            .setBgColor(Color.parseColor("#FF3D00"))
            .setShapeRadius(8f)
            .setShadowColor(Color.parseColor("#991DE9B6"))
            .setShadowRadius(6f)
            .setOffsetX(4f)
            .setOffsetY(4f)
            .into(text5)

        XSelector.shadowHelper()
            .setShapeRadius(8f)
            .setBgColor(Color.parseColor("#1DE9B6"))
            .setShadowColor(Color.parseColor("#4DA65740"))
            .setShadowRadius(8f)
            .setOffsetY(5f)
            .into(rl_bg)
    }
}