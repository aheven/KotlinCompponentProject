package heven.holt.model.camera.ui

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import heven.holt.lib_common.base.BaseActivity
import heven.holt.lib_common.utils.xselector.XSelector
import heven.holt.lib_common.utils.xselector.selector.ColorSelector
import heven.holt.lib_common.utils.xselector.selector.CompoundDrawableSelector
import heven.holt.model.camera.R
import kotlinx.android.synthetic.main.camera_activity_xselector.*

class XSelectorActivity : BaseActivity() {
    override fun getLayoutResID(): Int = R.layout.camera_activity_xselector

    override fun initActivity(savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        initEditText()
        initButton()
        initTextView()
    }

    private fun initEditText() {
        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_white)
            .defaultStrokeColor(R.color.camera_defaultColor)
            .focusedBgColor(R.color.camera_yellow)
            .focusedStrokeColor(R.color.camera_defaultColor)
            .strokeWidth(2)
            .into(et1)
            .colorSelector()
            .defaultColor(R.color.camera_gray)
            .focusedColor(R.color.camera_red)
            .textType(ColorSelector.HINT_TEXT_COLOR)
            .into(et1)
            .colorSelector()
            .defaultColor(R.color.camera_gray)
            .focusedColor(R.color.camera_black)
            .into(et1)

        XSelector.shapeSelector()
            .defaultStrokeColor(R.color.camera_gray)
            .focusedStrokeColor(R.color.camera_yellow)
            .strokeWidth(2)
            .radius(5f)
            .into(et2)
            .colorSelector()
            .defaultColor(R.color.camera_gray)
            .focusedColor(R.color.camera_black)
            .textType(ColorSelector.HINT_TEXT_COLOR)
            .into(et2)
            .colorSelector()
            .defaultColor(R.color.camera_black)
            .focusedColor(R.color.camera_yellow)
            .into(et2)

        XSelector.shapeSelector()
            .defaultStrokeColor(R.color.camera_gray)
            .focusedStrokeColor(R.color.camera_red)
            .strokeWidth(2)
            .into(et3)
            .colorSelector()
            .defaultColor(R.color.camera_gray)
            .focusedColor(R.color.camera_black)
            .textType(ColorSelector.HINT_TEXT_COLOR)
            .into(et3)
            .colorSelector()
            .defaultColor(R.color.camera_gray)
            .focusedColor(R.color.camera_red)
            .into(et3)
    }

    private fun initButton() {
        XSelector.drawableSelector()
            .defaultDrawable(R.mipmap.camera_blue_primary)
            .pressedDrawable(R.mipmap.camera_blue_primary_dark)
            .into(btn1)

        XSelector.shapeSelector()
            .defaultBgColor(android.R.color.holo_blue_light)
            .pressedBgColor(android.R.color.holo_blue_dark)
            .into(btn2)

        XSelector.shapeSelector()
            .defaultBgColor(android.R.color.holo_blue_light)
            .pressedBgColor(android.R.color.holo_blue_dark)
            .selectedBgColor(android.R.color.holo_red_light)
            .radius(5f)
            .into(btn3)
        btn3.setOnClickListener { btn3.isSelected = !btn3.isSelected }

        XSelector.shapeSelector()
            .defaultBgColor(android.R.color.holo_blue_light)
            .pressedBgColor(android.R.color.holo_blue_dark)
            .disabledBgColor(R.color.camera_gray)
            .into(btn4)
        btn4.isEnabled = false
    }

    private fun initTextView() {
        XSelector.shapeSelector()
            .defaultStrokeColor(R.color.camera_gray)
            .strokeWidth(1)
            .radius(5f)
            .into(tv1)
        XSelector.colorSelector()
            .defaultColor(R.color.camera_black)
            .pressedColor(R.color.camera_red)
            .into(tv2)
        XSelector.colorSelector()
            .defaultColor(R.color.camera_black)
            .selectedColor(R.color.camera_red)
            .into(tv3)
        tv3.setOnClickListener { tv3.isSelected = !tv3.isSelected }
        XSelector.colorSelector()
            .defaultColor(R.color.camera_black)
            .selectedColor(R.color.camera_yellow)
            .disabledColor(R.color.camera_gray)
            .into(tv4)
        tv4.isEnabled = false

        XSelector.compoundDrawableSelector()
            .setDrawablePadding(5f)
            .setDrawableOrientation(CompoundDrawableSelector.TOP)
            .defaultDrawable(R.mipmap.ic_launcher)
            .pressedDrawable(R.color.camera_black)
//            .selectedDrawable(R.mipmap.camera_blue_primary)
            .into(tv5)
    }

    fun clickButton(view: View) {
        when (view.id) {
            R.id.btn_shape -> ActivityUtils.startActivity(XSelectorShapeActivity::class.java)
            R.id.btn_shade -> ActivityUtils.startActivity(XSelectorShadowActivity::class.java)
        }
    }
}