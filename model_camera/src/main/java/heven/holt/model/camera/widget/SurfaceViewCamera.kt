package heven.holt.model.camera.widget

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import heven.holt.model.camera.utils.CameraUtils

class SurfaceViewCamera(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    var touchPointListener: ((point: PointF) -> Unit)? = null
    private val touchPoint = PointF()

    init {
        initView()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        CameraUtils.startPreviewDisplay(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        CameraUtils.releaseCamera()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        CameraUtils.openFrontalCamera(CameraUtils.DESIRED_PREVIEW_FPS)
    }

    private fun initView() {
        holder.addCallback(this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchPoint.x = event.x
                touchPoint.y = event.y
                touchPointListener?.invoke(touchPoint)
            }
        }
        return super.onTouchEvent(event)
    }
}