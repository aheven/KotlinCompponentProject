package heven.holt.lib_common.utils.xselector.selector

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringDef
import heven.holt.lib_common.utils.xselector.XSelector
import heven.holt.lib_common.utils.xselector.`interface`.ISelectorUtil
import heven.holt.lib_common.utils.xselector.util.XSelectorHelper.dip2px

class CompoundDrawableSelector private constructor() : ISelectorUtil<Drawable, TextView> {
    companion object {
        const val LEFT = "LEFT"
        const val RIGHT = "RIGHT"
        const val TOP = "TOP"
        const val BOTTOM = "BOTTOM"

        fun getInstance() = CompoundDrawableSelector()
    }

    private val drawableSelector = DrawableSelector.getInstance()

    private var drawablePadding = 0f //绘制间隔
    @DrawableOrientation
    private var drawableOrientation = LEFT

    @StringDef(LEFT, RIGHT, TOP, BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class DrawableOrientation

    fun defaultDrawable(@DrawableRes drawableRes: Int): CompoundDrawableSelector {
        drawableSelector.defaultDrawable(drawableRes)
        return this
    }

    fun disabledDrawable(@DrawableRes drawableRes: Int): CompoundDrawableSelector {
        drawableSelector.disabledDrawable(drawableRes)
        return this
    }

    fun pressedDrawable(@DrawableRes drawableRes: Int): CompoundDrawableSelector {
        drawableSelector.pressedDrawable(drawableRes)
        return this
    }

    fun selectedDrawable(@DrawableRes drawableRes: Int): CompoundDrawableSelector {
        drawableSelector.selectedDrawable(drawableRes)
        return this
    }

    fun focusedDrawable(@DrawableRes drawableRes: Int): CompoundDrawableSelector {
        drawableSelector.focusedDrawable(drawableRes)
        return this
    }

    fun setDrawablePadding(drawablePadding: Float): CompoundDrawableSelector {
        this.drawablePadding = drawablePadding
        return this
    }

    /**
     * 设置绘制方向 例：CompoundDrawableSelector.TOP
     */
    fun setDrawableOrientation(@DrawableOrientation drawableOrientation: String): CompoundDrawableSelector {
        this.drawableOrientation = drawableOrientation
        return this
    }

    fun defaultDrawable(drawable: Drawable): CompoundDrawableSelector {
        drawableSelector.defaultDrawable(drawable)
        return this
    }

    fun disabledDrawable(drawable: Drawable): CompoundDrawableSelector {
        drawableSelector.disabledDrawable(drawable)
        return this
    }

    fun pressedDrawable(drawable: Drawable): CompoundDrawableSelector {
        drawableSelector.pressedDrawable(drawable)
        return this
    }

    fun selectedDrawable(drawable: Drawable): CompoundDrawableSelector {
        drawableSelector.selectedDrawable(drawable)
        return this
    }

    fun focusedDrawable(drawable: Drawable): CompoundDrawableSelector {
        drawableSelector.focusedDrawable(drawable)
        return this
    }

    override fun into(view: TextView): XSelector {
        try {
            val drawable = build()
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            view.compoundDrawablePadding = dip2px(drawablePadding)
            when (drawableOrientation) {
                LEFT -> view.setCompoundDrawables(drawable, null, null, null)
                RIGHT -> view.setCompoundDrawables(null, null, drawable, null)
                TOP -> view.setCompoundDrawables(null, drawable, null, null)
                BOTTOM -> view.setCompoundDrawables(null, null, null, drawable)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw ExceptionInInitializerError("set compound drawable 必须是TextView或其子类！")
        }
        return XSelector
    }

    override fun build(): Drawable = drawableSelector.build()
}