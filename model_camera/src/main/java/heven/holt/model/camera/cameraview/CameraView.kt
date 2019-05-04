package heven.holt.model.camera.cameraview

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import heven.holt.model.camera.R
import heven.holt.model.camera.cameraview.api14.Camera1
import heven.holt.model.camera.cameraview.api9.SurfaceViewPreview
import heven.holt.model.camera.cameraview.base.*

class CameraView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {
    internal var mImpl: CameraViewImpl? = null

    private var mAdjustViewBounds: Boolean = false
    private var mZoomEnabled = true
    private var maximumWidth = 0
    private var maximumPreviewWidth = 0

    private var mDisplayOrientationDetector: DisplayOrientationDetector? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
// Attributes
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.CameraView,
            defStyleAttr,
            R.style.camera_Widget_CameraView
        )
        mAdjustViewBounds = a.getBoolean(R.styleable.CameraView_android_adjustViewBounds, false)
        val aspectRatio = a.getString(R.styleable.CameraView_cameraAspectRatio)
        maximumWidth = a.getInt(R.styleable.CameraView_camera_maximumWidth, 0)
        maximumPreviewWidth = a.getInt(R.styleable.CameraView_camera_maximumPreviewWidth, 0)
        val useHighResPicture = a.getBoolean(R.styleable.CameraView_camera_useHighResPicture, maximumWidth == 0)
        val facing = a.getInt(R.styleable.CameraView_camera_facing, FACING_BACK)
        val autoFocus = a.getBoolean(R.styleable.CameraView_camera_autoFocus, true)
        val flash = a.getInt(R.styleable.CameraView_camera_flash, FLASH_AUTO)
        val zoomEnabled = a.getBoolean(R.styleable.CameraView_camera_enableZoom, true)
        a.recycle()

        // Internal setup
        val preview = createPreviewImpl(context, false)
//        if (CameraViewConfig.isForceCamera1 || Build.VERSION.SDK_INT < 21) {
        mImpl = Camera1(context, preview)
//        }
//        else if (Build.VERSION.SDK_INT >= 23 && useHighResPicture) {
//            mImpl = Camera2Api23(preview, context)
//        } else {
//            mImpl = Camera2(preview, context)
//        }
        mImpl!!.maximumWidth = maximumWidth
        mImpl!!.maximumPreviewWidth = maximumPreviewWidth

        setFacing(facing)
        setAutoFocus(autoFocus)
        setFlash(flash)
        setZoomEnabled(zoomEnabled)
        if (aspectRatio != null) {
            setAspectRatio(AspectRatio.parse(aspectRatio), true)
        } else {
            setAspectRatio(DEFAULT_ASPECT_RATIO, true)
        }

        // Display orientation detector
        mDisplayOrientationDetector = object : DisplayOrientationDetector(context) {
            override fun onDisplayOrientationChanged(displayOrientation: Int) {
                mImpl?.setDisplayOrientation(displayOrientation)
            }
        }
    }

    private fun createPreviewImpl(context: Context, isLegacy: Boolean): PreviewImpl {
        val preview: PreviewImpl
//        if (Build.VERSION.SDK_INT < 21 || CameraViewConfig.isForceCamera1 || isLegacy) {
        preview = SurfaceViewPreview(context, this)
//        } else {
//            preview = TextureViewPreview(context, this)
//        }
        return preview
    }

    /**
     * Chooses camera by the direction it faces.
     *
     * @param facing The camera facing. Must be either [.FACING_BACK] or
     * [.FACING_FRONT].
     */
    fun setFacing(@Facing facing: Int) {
        mImpl?.setFacing(facing)
    }

    /**
     * Enables or disables the continuous auto-focus mode. When the current camera doesn't support
     * auto-focus, calling this method will be ignored.
     *
     * @param autoFocus `true` to enable continuous auto-focus mode. `false` to
     * disable it.
     */
    fun setAutoFocus(autoFocus: Boolean) {
        mImpl?.setAutoFocus(autoFocus)
    }

    /**
     * Sets the flash mode.
     *
     * @param flash The desired flash mode.
     */
    fun setFlash(@Flash flash: Int) {
        mImpl?.setFlash(flash)
    }

    /**
     * Enables or disables the zoom on pitch.
     *
     * @param zoomOnPitch `true` to enable zoom on pitch. `false` to disable it.
     */
    fun setZoomEnabled(zoomOnPitch: Boolean) {
        mZoomEnabled = zoomOnPitch
    }

    /**
     * Sets the aspect ratio of camera.
     *
     * @param ratio The [AspectRatio] to be set.
     */
    fun setAspectRatio(ratio: AspectRatio, isInitializing: Boolean) {
        if (mImpl?.setAspectRatio(ratio, isInitializing) == true) {
            requestLayout()
        }
    }

    /**
     * Open a camera device and start showing camera preview. This is typically called from
     * [Activity.onResume].
     */
    fun start() {
        if (!mImpl!!.start()) {
            //store the state ,and restore this state after fall back o Camera1
            val state = onSaveInstanceState()
            // Camera2 uses legacy hardware layer; fall back to Camera1
            mImpl = Camera1(context, createPreviewImpl(context, true))
            mImpl!!.maximumWidth = maximumWidth
            mImpl!!.maximumPreviewWidth = maximumPreviewWidth
            onRestoreInstanceState(state)
            mImpl!!.start()
        }
    }

    fun setOnPictureTakenListener(pictureTakenListener: CameraViewImpl.OnPictureTakenListener) {
        if (mImpl != null) {
            mImpl!!.setOnPictureTakenListener(pictureTakenListener)
        }
    }

    fun setOnPictureBytesAvailableListener(pictureBytesAvailableListener: CameraViewImpl.OnPictureBytesAvailableListener) {
        if (mImpl != null) {
            mImpl!!.setOnPictureBytesAvailableListener(pictureBytesAvailableListener)
        }
    }

    fun setOnFocusLockedListener(focusLockedListener: CameraViewImpl.OnFocusLockedListener) {
        if (mImpl != null) {
            mImpl!!.setOnFocusLockedListener(focusLockedListener)
        }
    }

    fun setOnTurnCameraFailListener(turnCameraFailListener: CameraViewImpl.OnTurnCameraFailListener) {
        if (mImpl != null) {
            mImpl!!.setOnTurnCameraFailListener(turnCameraFailListener)
        }
    }

    fun setOnCameraErrorListener(cameraErrorListener: CameraViewImpl.OnCameraErrorListener) {
        if (mImpl != null) {
            mImpl!!.setOnCameraErrorListener(cameraErrorListener)
        }
    }

    fun setOnFrameListener(onFrameListener: CameraViewImpl.OnFrameListener) {
        if (mImpl != null) {
            mImpl!!.setOnFrameListener(onFrameListener)
        }
    }

    fun stop() {
        mImpl?.stop()
    }
}