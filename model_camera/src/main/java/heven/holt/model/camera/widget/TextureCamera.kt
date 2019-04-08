package heven.holt.model.camera.widget

import android.content.Context
import android.graphics.PointF
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.TextureView
import heven.holt.model.camera.utils.CameraUtils

class TextureCamera(context: Context, attrs: AttributeSet) : TextureView(context, attrs),
    TextureView.SurfaceTextureListener {
    var surface: SurfaceTexture? = null
    private val touchPoint = PointF()
    var touchPointListener: ((point: PointF) -> Unit)? = null

    init {
        surfaceTextureListener = this
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        CameraUtils.releaseCamera()
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        this.surface = surface
        CameraUtils.openFrontalCamera(CameraUtils.DESIRED_PREVIEW_FPS)
        CameraUtils.startPreviewTexture(surface)
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