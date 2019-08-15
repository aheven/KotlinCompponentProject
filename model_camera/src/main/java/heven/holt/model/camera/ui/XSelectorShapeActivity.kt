package heven.holt.model.camera.ui

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import heven.holt.lib_common.base.BaseActivity
import heven.holt.lib_common.utils.xselector.XSelector
import heven.holt.lib_common.utils.xselector.selector.ShapeSelector
import heven.holt.model.camera.R
import kotlinx.android.synthetic.main.camera_activity_xselector_shapre.*

class XSelectorShapeActivity : BaseActivity() {
    override fun getLayoutResID(): Int = R.layout.camera_activity_xselector_shapre

    override fun initActivity(savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        //圆形
        XSelector.shapeSelector()
            .setShape(GradientDrawable.OVAL)
            .defaultBgColor(R.color.camera_colorAccent)
            .pressedBgColor(R.color.camera_defaultColor)
            .into(tv_oval_solid)

        XSelector.shapeSelector()
            .setShape(GradientDrawable.OVAL)
            .defaultBgColor(R.color.camera_colorAccent)
            .defaultStrokeColor(R.color.camera_defaultColor)
            .strokeWidth(1)
            .into(tv_oval_line)

        XSelector.shapeSelector()
            .setShape(GradientDrawable.OVAL)
            .defaultBgColor(R.color.camera_colorAccent)
            .dashLine(1, R.color.camera_defaultColor, 5f, 5f)
            .into(tv_oval_dash_line)

        //矩形
        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .radius(5f)
            .into(tv_rect_solid_radius)

        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .defaultStrokeColor(R.color.camera_defaultColor)
            .strokeWidth(1)
            .radius(5f)
            .into(tv_rect_line_radius)

        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .dashLine(1, R.color.camera_defaultColor, 5f, 5f)
            .radius(5f)
            .into(tv_rect_dash_line_radius)

        //圆角（常用）
        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .radius(999f)
            .into(tv_rect_solid)

        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .defaultStrokeColor(R.color.camera_defaultColor)
            .strokeWidth(1)
            .radius(999f)
            .into(tv_rect_line)

        //触摸反馈
        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .pressedBgColor(R.color.camera_defaultColor)
            .radius(999f)
            .into(btn_selector_background)

        XSelector.selectorBackground(
            XSelector.shapeSelector().defaultBgColor(R.color.camera_defaultColor).radius(999f).build(),
            XSelector.shapeSelector().defaultBgColor(R.color.camera_colorAccent).radius(999f).build()
        ).selectorColor("#ffffff", "#000000")
            .into(btn_selector_background_color)

        //圆角
        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .tlRadius(10f)
            .trRadius(10f)
            .into(tv_rect_top_radius)

        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .blRadius(10f)
            .brRadius(10f)
            .into(tv_rect_bottom_radius)

        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .tlRadius(10f)
            .brRadius(10f)
            .into(tv_rect_diagonal1)

        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .trRadius(10f)
            .blRadius(10f)
            .into(tv_rect_diagonal2)

        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .tlRadius(10f)
            .into(tv_rect_tl_radius)

        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .trRadius(10f)
            .into(tv_rect_tr_radius)

        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .blRadius(10f)
            .into(tv_rect_bl_radius)

        XSelector.shapeSelector()
            .defaultBgColor(R.color.camera_colorAccent)
            .brRadius(10f)
            .into(tv_rect_br_radius)

        //渐变
        XSelector.shapeSelector()
            .gradientLinear(R.color.camera_colorAccent, R.color.camera_defaultColor)
            .into(tv_gradient_linear_tb)

        XSelector.shapeSelector()
            .gradientLinear(ShapeSelector.BOTTOM_TOP,R.color.camera_colorAccent, R.color.camera_defaultColor)
            .into(tv_gradient_linear_bt)

        XSelector.shapeSelector()
            .gradientLinear(ShapeSelector.LEFT_RIGHT,R.color.camera_colorAccent, R.color.camera_defaultColor)
            .into(tv_gradient_linear_lr)

        XSelector.shapeSelector()
            .gradientLinear(ShapeSelector.RIGHT_LEFT,R.color.camera_colorAccent, R.color.camera_defaultColor)
            .into(tv_gradient_linear_rl)

        XSelector.shapeSelector()
            .setShape(GradientDrawable.OVAL)
            .gradientSweep(R.color.camera_colorAccent, R.color.camera_defaultColor)
            .into(tv_gradient_sweep)

        XSelector.shapeSelector()
            .setShape(GradientDrawable.OVAL)
            .gradientRadial(30f,R.color.camera_colorAccent, R.color.camera_defaultColor)
            .into(tv_gradient_radial)
    }
}