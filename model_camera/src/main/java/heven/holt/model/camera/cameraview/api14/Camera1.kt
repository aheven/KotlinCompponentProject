@file:Suppress("DEPRECATION")
package heven.holt.model.camera.cameraview.api14

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import androidx.collection.SparseArrayCompat
import com.blankj.utilcode.util.LogUtils
import heven.holt.model.camera.BuildConfig
import heven.holt.model.camera.cameraview.base.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean


class Camera1(context: Context, preview: PreviewImpl) : CameraViewImpl(context, preview) {
    private val INVALID_CAMERA_ID = -1

    private val FLASH_MODES = SparseArrayCompat<String>().apply {
        put(FLASH_OFF, Camera.Parameters.FLASH_MODE_OFF)
        put(FLASH_ON, Camera.Parameters.FLASH_MODE_ON)
        put(FLASH_TORCH, Camera.Parameters.FLASH_MODE_TORCH)
        put(FLASH_AUTO, Camera.Parameters.FLASH_MODE_AUTO)
        put(FLASH_RED_EYE, Camera.Parameters.FLASH_MODE_RED_EYE)
    }

    private var mCameraId: Int = 0

    private val isPictureCaptureInProgress = AtomicBoolean(false)

    private var mCamera: Camera? = null
    private var mCameraParameters: Camera.Parameters? = null

    private val mCameraInfo = Camera.CameraInfo()

    private val mPreviewSizes = SizeMap()
    private val mPictureSizes = SizeMap()

    private var mAspectRatio: AspectRatio? = null

    private var mShowingPreview: Boolean = false

    private var mAutoFocus: Boolean = false

    private var mFacing: Int = 0

    private var mFlash: Int = 0

    private var mDisplayOrientation: Int = 0

    private var mFrameHandler: Handler? = null
    private var mFrameThread: HandlerThread? = null

    protected var mZoomDistance: Float? = null


    init {
        preview.setCallback(object : PreviewImpl.Callback {
            override fun onSurfaceChanged() {
                if (mCamera != null) {
                    setUpPreview()
                    adjustCameraParameters()
                    setupPreviewCallback()
                }
            }
        })
    }

    override fun start(): Boolean {
        orientation.startListening(orientationListener)
        chooseCamera()
        openCamera()
        if (mPreview.isReady()) {
            setUpPreview()
            setupPreviewCallback()
        }
        mShowingPreview = true
        startBackgroundThread()
        mCamera?.startPreview()
        return true
    }

    override fun stop() {
        orientation.stopListening()
        latestFrameWidth = 0
        latestFrameHeight = 0
        stopBackgroundThread()
        if (mCamera != null) {
            mCamera?.stopPreview()
        }
        mShowingPreview = false
        releaseCamera()
    }

    override fun isCameraOpened(): Boolean = mCamera != null

    override fun setFacing(facing: Int) {
        if (mFacing == facing) {
            return
        }
        mFacing = facing
        if (isCameraOpened()) {
            stop()
            start()
        }
    }

    override fun getFacing(): Int = mFacing

    override fun getSupportedAspectRatios(): Set<AspectRatio> {
        val idealAspectRatios = mPreviewSizes
        for (aspectRatio in idealAspectRatios.ratios()) {
            if (mPictureSizes.sizes(aspectRatio) == null) {
                idealAspectRatios.remove(aspectRatio)
            }
        }
        return idealAspectRatios.ratios()
    }

    override fun setAspectRatio(ratio: AspectRatio, isInitializing: Boolean): Boolean {
        if (mAspectRatio == null || !isCameraOpened()) {
            // Handle this later when camera is opened
            mAspectRatio = ratio
            return true
        } else if (mAspectRatio!! != ratio) {
            val sizes = mPreviewSizes.sizes(ratio)
            if (sizes == null) {
                throw UnsupportedOperationException("$ratio is not supported")
            } else {
                mAspectRatio = ratio
                adjustCameraParameters()
                return true
            }
        }
        return false
    }

    override fun getAspectRatio(): AspectRatio = mAspectRatio!!

    override fun setAutoFocus(autoFocus: Boolean) {
        if (mAutoFocus == autoFocus) {
            return
        }
        if (setAutoFocusInternal(autoFocus)) {
            mCamera?.parameters = mCameraParameters
        }
    }

    override fun getAutoFocus(): Boolean {
        if (!isCameraOpened()) {
            return mAutoFocus
        }
        val focusMode = mCameraParameters?.focusMode
        return focusMode != null && focusMode.contains("continuous")
    }

    override fun setFlash(flash: Int) {
        if (flash == mFlash) {
            return
        }
        if (setFlashInternal(flash)) {
            mCamera?.parameters = mCameraParameters
        }
    }

    override fun getFlash(): Int = mFlash

    override fun takePicture() {
        if (!isCameraOpened()) {
            throw IllegalStateException(
                "Camera is not ready. Call start() before takePicture()."
            )
        }
        if (getAutoFocus()) {
            mCamera?.cancelAutoFocus()
            mCamera?.autoFocus { _, _ -> takePictureInternal() }
        } else {
            takePictureInternal()
        }
    }

    override fun setDisplayOrientation(displayOrientation: Int) {
        try {
            if (mDisplayOrientation == displayOrientation) {
                return
            }
            mDisplayOrientation = displayOrientation
            if (isCameraOpened()) {
                mCameraParameters?.setRotation(displayOrientation)
                mCamera?.parameters = mCameraParameters
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
            if (cameraErrorCallback != null) {
                mPreview.getView().post { cameraErrorCallback?.onCameraError(e) }
            }
        }

    }

    override fun getCameraDefaultOrientation(): Int {
        return if (getFacing() == FACING_FRONT) mCameraInfo.orientation - 180 else mCameraInfo.orientation
    }

    override fun zoom(event: MotionEvent): Boolean {
        try {
            val params = mCamera?.parameters
            val maxZoom = params?.maxZoom ?: 0
            var zoom = params?.zoom ?: 0
            val realTimeDistance = getFingerSpacing(event)
            if (mZoomDistance == null) {
                mZoomDistance = realTimeDistance
                return true
            }

            var needZoom = false
            var deltaZoom = maxZoom / 30 + 1
            when {
                realTimeDistance - mZoomDistance!! >= pixelsPerOneZoomLevel -> {
                    //zoom in
                    if (zoom < maxZoom) {
                        if (zoom + deltaZoom > maxZoom) deltaZoom = maxZoom - zoom
                        zoom += deltaZoom
                    }
                    needZoom = true
                }
                mZoomDistance!! - realTimeDistance >= pixelsPerOneZoomLevel -> {
                    //zoom out
                    if (zoom > 0) {
                        if (zoom - deltaZoom < 1) deltaZoom = zoom - 1
                        zoom -= deltaZoom
                    }
                    needZoom = true
                }
                else -> {
                    //Do nothing since the difference is not large enough
                }
            }
            if (needZoom) {
                mZoomDistance = realTimeDistance
                params?.zoom = zoom
                mCamera?.parameters = params
            }
            return true
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
            if (cameraErrorCallback != null) {
                mPreview.getView().post { cameraErrorCallback?.onCameraError(e) }
            }
            return false
        }

    }

    override fun onPinchFingerUp() {
        mZoomDistance = null
    }

    private fun startBackgroundThread() {
        mFrameThread = HandlerThread("CameraFrameBackground")
        mFrameThread?.start()
        mFrameHandler = Handler(mFrameThread!!.looper)
    }

    private fun stopBackgroundThread() {
        try {
            if (mFrameThread != null) mFrameThread!!.quit()
            if (mFrameThread != null) mFrameThread!!.join()
            mFrameThread = null
            mFrameHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    private fun takePictureInternal() {
        stopBackgroundThread()
        try {
            if (!isPictureCaptureInProgress.getAndSet(true)) {
                mCamera?.takePicture(
                    Camera.ShutterCallback { if (focusLockedCallback != null) focusLockedCallback?.onFocusLocked() },
                    null,
                    null,
                    Camera.PictureCallback { data, camera ->
                        isPictureCaptureInProgress.set(false)
                        if (pictureBytesCallback != null) pictureBytesCallback?.onPictureBytesAvailable(
                            data,
                            getRotationDegrees()
                        )
                        byteArrayToBitmap(data)
                        camera.cancelAutoFocus()
                        camera.startPreview()
                        startBackgroundThread()
                    })
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
            if (cameraErrorCallback != null) {
                mPreview.getView().post { cameraErrorCallback?.onCameraError(e) }
            }
            startBackgroundThread()
        }

    }

    /**
     * This rewrites [.mCameraId] and [.mCameraInfo].
     */
    private fun chooseCamera() {
        var i = 0
        val count = Camera.getNumberOfCameras()
        while (i < count) {
            Camera.getCameraInfo(i, mCameraInfo)
            if (mCameraInfo.facing == mFacing) {
                mCameraId = i
                return
            }
            i++
        }
        mCameraId = INVALID_CAMERA_ID
        if (turnFailCallback != null) turnFailCallback?.onTurnCameraFail(RuntimeException("Cannot find suitable camera."))
    }

    private fun openCamera() {
        try {
            if (mCamera != null) {
                releaseCamera()
            }
            mCamera = Camera.open(mCameraId)
            mCameraParameters = mCamera!!.parameters
            // Supported preview sizes
            mPreviewSizes.clear()
            for (size in mCameraParameters!!.supportedPreviewSizes) {
                if (maximumPreviewWidth == 0) {
                    mPreviewSizes.add(Size(size.width, size.height))
                } else if (size.width <= maximumPreviewWidth && size.height <= maximumPreviewWidth) {
                    mPreviewSizes.add(Size(size.width, size.height))
                }
            }
            // Supported picture sizes;
            mPictureSizes.clear()
            for (size in mCameraParameters!!.supportedPictureSizes) {
                Log.i("CameraView2", "Picture Size: $size")
                if (maximumWidth == 0) {
                    mPictureSizes.add(Size(size.width, size.height))
                } else if (size.width <= maximumWidth && size.height <= maximumWidth) {
                    mPictureSizes.add(Size(size.width, size.height))
                }
            }
            // AspectRatio
            if (mAspectRatio == null) {
                mAspectRatio = DEFAULT_ASPECT_RATIO
            }
            adjustCameraParameters()
            mCamera?.setDisplayOrientation(calcDisplayOrientation(mDisplayOrientation))
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
            if (cameraErrorCallback != null) {
                mPreview.getView().post { cameraErrorCallback?.onCameraError(e) }
            }
        }

    }

    private fun releaseCamera() {
        if (mCamera != null) {
            mCamera?.setPreviewCallback(null)
            mCamera?.release()
            mCamera = null
        }
    }

    // Suppresses Camera#setPreviewTexture
    @SuppressLint("NewApi")
    fun setUpPreview() {
        try {
            if (mPreview.getOutputClass() === SurfaceHolder::class.java) {
                mCamera?.setPreviewDisplay(mPreview.getSurfaceHolder())
            } else {
                mCamera?.setPreviewTexture(mPreview.getSurfaceTexture() as SurfaceTexture)
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
            if (cameraErrorCallback != null) {
                mPreview.getView().post { cameraErrorCallback?.onCameraError(e) }
            }
        }

    }

    private var latestFrameData: ByteArray? = null
    private var latestFrameWidth: Int = 0
    private var latestFrameHeight: Int = 0
    fun setupPreviewCallback() {
        try {
            mCamera?.setPreviewCallback(Camera.PreviewCallback { data, camera ->
                if (data == null || isPictureCaptureInProgress.get() || mFrameHandler == null) return@PreviewCallback
                if (onFrameCallback != null) {
                    latestFrameData = data
                    if (latestFrameWidth == 0) latestFrameWidth = camera.parameters.previewSize.width
                    if (latestFrameHeight == 0) latestFrameHeight = camera.parameters.previewSize.height
                    mFrameHandler?.post {
                        onFrameCallback?.onFrame(
                            latestFrameData!!,
                            latestFrameWidth,
                            latestFrameHeight,
                            getRotationDegrees()
                        )
                    }
                }
            })
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
            if (cameraErrorCallback != null) {
                mPreview.getView().post { cameraErrorCallback?.onCameraError(e) }
            }
        }

    }

    private fun chooseAspectRatio(): AspectRatio? {
        var r: AspectRatio? = null
        for (ratio in mPreviewSizes.ratios()) {
            r = ratio
            if (ratio == DEFAULT_ASPECT_RATIO) {
                return ratio
            }
        }
        return r
    }

    fun adjustCameraParameters() {
        try {
            if (mAspectRatio == null) {
                LogUtils.e("mAspectRatio is null")
                return
            }
            var sizes = mPreviewSizes.sizes(mAspectRatio!!)
            if (sizes == null) { // Not supported
                mAspectRatio = chooseAspectRatio()
                sizes = mPreviewSizes.sizes(mAspectRatio!!)
            }
            mPreviewSizeSelected = chooseOptimalSize(sizes!!)

            // Always re-apply camera parameters
            // Largest picture size in this ratio
            mPictureSizeSelected = mPictureSizes.sizes(mAspectRatio!!)!!.last()
            if (mShowingPreview) {
                mCamera?.stopPreview()
            }
            mCameraParameters?.setPreviewSize(mPreviewSizeSelected!!.mWidth, mPreviewSizeSelected!!.mHeight)
            mCameraParameters?.setPictureSize(mPictureSizeSelected!!.mWidth, mPictureSizeSelected!!.mHeight)
            mCameraParameters?.setRotation(mDisplayOrientation)
            setAutoFocusInternal(mAutoFocus)
            setFlashInternal(mFlash)
            mCamera?.parameters = mCameraParameters
            if (mShowingPreview) {
                mCamera?.startPreview()
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
            if (cameraErrorCallback != null) {
                mPreview.getView().post { cameraErrorCallback?.onCameraError(e) }
            }
        }

    }

    /**
     * @return `true` if [.mCameraParameters] was modified.
     */
    private fun setFlashInternal(flash: Int): Boolean {
        try {
            if (isCameraOpened()) {
                val modes = mCameraParameters?.supportedFlashModes
                val mode = FLASH_MODES.get(flash)
                if (modes != null && modes.contains(mode)) {
                    mCameraParameters?.flashMode = mode
                    mFlash = flash
                    return true
                }
                val currentMode = FLASH_MODES.get(mFlash)
                if (modes == null || !modes.contains(currentMode)) {
                    mCameraParameters?.flashMode = Camera.Parameters.FLASH_MODE_OFF
                    mFlash = FLASH_OFF
                    return true
                }
                return false
            } else {
                mFlash = flash
                return false
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
            if (cameraErrorCallback != null) {
                mPreview.getView().post { cameraErrorCallback?.onCameraError(e) }
            }
            return false
        }

    }

    /**
     * @return `true` if [.mCameraParameters] was modified.
     */
    private fun setAutoFocusInternal(autoFocus: Boolean): Boolean {
        try {
            mAutoFocus = autoFocus
            return if (isCameraOpened()) {
                val modes = mCameraParameters!!.supportedFocusModes
                if (autoFocus && modes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    mCameraParameters?.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                } else if (modes.contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
                    mCameraParameters?.focusMode = Camera.Parameters.FOCUS_MODE_FIXED
                } else if (modes.contains(Camera.Parameters.FOCUS_MODE_INFINITY)) {
                    mCameraParameters?.focusMode = Camera.Parameters.FOCUS_MODE_INFINITY
                } else {
                    mCameraParameters?.focusMode = modes[0]
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
            if (cameraErrorCallback != null) {
                mPreview.getView().post { cameraErrorCallback?.onCameraError(e) }
            }
            return false
        }

    }

    /**
     * Test if the supplied orientation is in landscape.
     *
     * @param orientationDegrees Orientation in degrees (0,90,180,270)
     * @return True if in landscape, false if portrait
     */
    private fun isLandscape(orientationDegrees: Int): Boolean {
        return orientationDegrees == LANDSCAPE_90 || orientationDegrees == LANDSCAPE_270
    }

    /**
     * Calculate display orientation
     * https://developer.android.com/reference/android/hardware/Camera.html#setDisplayOrientation(int)
     *
     * This calculation is used for orienting the preview
     *
     * Note: This is not the same calculation as the camera rotation
     *
     * @param screenOrientationDegrees Screen orientation in degrees
     * @return Number of degrees required to rotate preview
     */
    private fun calcDisplayOrientation(screenOrientationDegrees: Int): Int {
        return if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            (360 - (mCameraInfo.orientation + screenOrientationDegrees) % 360) % 360
        } else {  // back-facing
            (mCameraInfo.orientation - screenOrientationDegrees + 360) % 360
        }
    }

    private fun chooseOptimalSize(sizes: SortedSet<Size>): Size? {
        if (!mPreview.isReady()) { // Not yet laid out
            return sizes.first() // Return the smallest size
        }
        val desiredWidth: Int
        val desiredHeight: Int
        val surfaceWidth = mPreview.getWidth()
        val surfaceHeight = mPreview.getHeight()
        if (isLandscape(mDisplayOrientation)) {
            desiredWidth = surfaceHeight
            desiredHeight = surfaceWidth
        } else {
            desiredWidth = surfaceWidth
            desiredHeight = surfaceHeight
        }
        var result: Size? = null
        for (size in sizes) { // Iterate from small to large
            if (desiredWidth <= size.mWidth && desiredHeight <= size.mHeight) {
                return size

            }
            result = size
        }
        return result
    }
}