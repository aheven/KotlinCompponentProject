package heven.holt.lib_common.utils.xselector.util

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.blankj.utilcode.util.SizeUtils
import heven.holt.lib_common.utils.xselector.XSelector

object XSelectorHelper {
    fun getColorRes(@ColorRes colorRes: Int) = XSelector.getContext().resources.getColor(colorRes)

    fun getDrawableRes(@DrawableRes drawableRes: Int): Drawable = XSelector.getContext().resources.getDrawable(drawableRes)

    fun dip2px(dipValue: Float): Int = SizeUtils.dp2px(dipValue)
}