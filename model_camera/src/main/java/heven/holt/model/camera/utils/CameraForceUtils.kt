package heven.holt.model.camera.utils

import android.graphics.Rect
import android.graphics.RectF


object CameraForceUtils {
    /**
     * 触摸坐标转换成相机坐标
     */
    fun calculateTapArea(x: Float, y: Float, coefficient: Int, width: Int, height: Int): Rect {
        val focusAreaSize = 200f
        val areaSize = java.lang.Float.valueOf(focusAreaSize * coefficient).toInt()
        val centerX = (x / width * 2000 - 1000).toInt()
        val centerY = (y / height * 2000 - 1000).toInt()
        val left = clamp(centerX - areaSize / 2, -1000, 1000)
        val top = clamp(centerY - areaSize / 2, -1000, 1000)
        val rectF = RectF(left.toFloat(), top.toFloat(), (left + areaSize).toFloat(), (top + areaSize).toFloat())
        return Rect(
            Math.round(rectF.left), Math.round(rectF.top),
            Math.round(rectF.right), Math.round(rectF.bottom)
        )
    }

    private fun clamp(x: Int, min: Int, max: Int): Int {
        if (x > max) return max
        if (x < min) return min
        return x
    }
}