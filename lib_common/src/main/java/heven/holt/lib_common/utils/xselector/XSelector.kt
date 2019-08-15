package heven.holt.lib_common.utils.xselector

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import heven.holt.lib_common.utils.xselector.selector.ColorSelector
import heven.holt.lib_common.utils.xselector.selector.CompoundDrawableSelector
import heven.holt.lib_common.utils.xselector.selector.DrawableSelector
import heven.holt.lib_common.utils.xselector.selector.ShapeSelector
import heven.holt.lib_common.utils.xselector.shadow.ShadowHelper
import heven.holt.lib_common.utils.xselector.util.XSelectorHelper

object XSelector {
    private var context: Application? = null

    /**
     * 必须在全局Application先调用，获取context上下文
     * @param app Application
     */
    fun init(app: Application) {
        context = app
    }

    /**
     * 获取上下文
     */
    fun getContext(): Context {
        initialize()
        return context!!
    }

    /**
     * 检测是否调用初始化方法
     */
    private fun initialize() {
        if (context == null) {
            throw ExceptionInInitializerError("请先在全局Application中调用XSelector.init()初始化！")
        }
    }

    /**
     *设置样式（主要是椭圆和矩形）
     */
    fun shapeSelector(): ShapeSelector = ShapeSelector.getInstance()

    /**
     * Drawable背景选择器
     */
    fun drawableSelector(): DrawableSelector = DrawableSelector.getInstance()

    /**
     * Color字体颜色选择器
     */
    fun colorSelector(): ColorSelector = ColorSelector.getInstance()

    /**
     * 阴影工具类
     */
    fun shadowHelper(): ShadowHelper = ShadowHelper.getInstance()

    /**
     *设置TextView及其子类的drawLeft
     */
    fun compoundDrawableSelector(): CompoundDrawableSelector = CompoundDrawableSelector.getInstance()

    /**
     * 背景状态选择器（背景颜色）
     *
     * @param pressedColorResId 触摸颜色 例：R.color.colorPrimary
     * @param normalColorResId  正常颜色 例：R.color.colorPrimary
     */
    fun selectorBackground(@ColorRes pressedColorResId: Int, @ColorRes normalColorResId: Int): DrawableSelector =
        DrawableSelector.getInstance().selectorBackground(
            ColorDrawable(XSelectorHelper.getColorRes(pressedColorResId)),
            ColorDrawable(XSelectorHelper.getColorRes(normalColorResId))
        )

    fun selectorBackground(pressedColor: String, normalColor: String): DrawableSelector =
        DrawableSelector.getInstance().selectorBackground(
            ColorDrawable(Color.parseColor(pressedColor)),
            ColorDrawable(Color.parseColor(normalColor))
        )

    /**
     * .
     * 背景状态选择器（背景Drawable）
     *
     * @param pressedDrawable 触摸颜色 例：Context.getResources.getDrawable(R.drawable/mipmap.xxx)
     * @param normalDrawable  正常颜色 例：Context.getResources.getDrawable(R.drawable/mipmap.xxx)
     */
    fun selectorBackground(pressedDrawable: Drawable, normalDrawable: Drawable): DrawableSelector =
        DrawableSelector.getInstance().defaultDrawable(normalDrawable).pressedDrawable(pressedDrawable)

}