package heven.holt.model.camera.cameraview.base

import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View

abstract class PreviewImpl {
    interface Callback {
        fun onSurfaceChanged()
    }

    private lateinit var mCallback: Callback

    private var mWidth = 0
    private var mHeight = 0

    fun setCallback(callback: Callback) {
        this.mCallback = callback
    }

    abstract fun getSurface(): Surface

    abstract fun getView(): View

    abstract fun getOutputClass(): Class<*>

    abstract fun setDisplayOrientation(displayOrientation: Int)

    abstract fun isReady(): Boolean

    protected fun dispatchSurfaceChanged() {
        mCallback.onSurfaceChanged()
    }

    open fun getSurfaceHolder(): SurfaceHolder? = null

    open fun getSurfaceTexture(): SurfaceTexture? = null

    fun setBufferSize(width: Int, height: Int) {}

    fun setSize(width: Int, height: Int) {
        mWidth = width
        mHeight = height
    }

    fun getWidth() = mWidth

    fun getHeight() = mHeight
}