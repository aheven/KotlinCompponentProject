package heven.holt.lib_common.utils.xselector.`interface`

import android.view.View
import heven.holt.lib_common.utils.xselector.XSelector

interface ISelectorUtil<T, V : View> {
    /**
     * 目标view
     * @param v 需要设置样式的view
     */
    fun into(view: V): XSelector

    fun build(): T
}