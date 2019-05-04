package heven.holt.model.camera.cameraview.api9

import android.content.Context
import android.view.*
import androidx.core.view.ViewCompat
import heven.holt.model.camera.R
import heven.holt.model.camera.cameraview.base.PreviewImpl

class SurfaceViewPreview(context: Context, parent: ViewGroup) : PreviewImpl() {
    private var mSurfaceView: SurfaceView? = null

    init {
        val view = View.inflate(context, R.layout.camera_surface_view, parent)
        mSurfaceView = view.findViewById<View>(R.id.surface_view) as SurfaceView
        val holder = mSurfaceView!!.holder
        //noinspection deprecation
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(h: SurfaceHolder) {}

            override fun surfaceChanged(h: SurfaceHolder, format: Int, width: Int, height: Int) {
                setSize(width, height)
                if (!ViewCompat.isInLayout(mSurfaceView!!)) {
                    dispatchSurfaceChanged()
                }
            }

            override fun surfaceDestroyed(h: SurfaceHolder) {
                setSize(0, 0)
            }
        })
    }

    override fun getSurface(): Surface = getSurfaceHolder().surface

    override fun getSurfaceHolder() = mSurfaceView!!.holder!!

    override fun getView(): View = mSurfaceView!!

    override fun getOutputClass(): Class<*> = SurfaceHolder::class.java

    override fun setDisplayOrientation(displayOrientation: Int) {
    }

    override fun isReady(): Boolean = getWidth() != 0 && getHeight() != 0
}