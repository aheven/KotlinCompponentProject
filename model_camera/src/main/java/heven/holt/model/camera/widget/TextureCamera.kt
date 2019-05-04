package heven.holt.model.camera.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.PointF
import android.graphics.SurfaceTexture
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.Surface
import android.view.TextureView
import heven.holt.model.camera.utils.Camera2Utils

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
        Camera2Utils.releaseCamera()
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        this.surface = surface
        Camera2Utils.getInstance(context, Surface(surfaceTexture),width, height)
        Camera2Utils.mPreviewSize?.let {
            surface?.setDefaultBufferSize(it.width,it.height)
        }
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