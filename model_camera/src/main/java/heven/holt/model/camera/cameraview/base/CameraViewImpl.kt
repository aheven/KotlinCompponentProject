package heven.holt.model.camera.cameraview.base

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.AsyncTask
import android.view.MotionEvent
import android.view.View

abstract class CameraViewImpl(context: Context, val mPreview: PreviewImpl) {
    /**
     * The distance between 2 fingers (in pixel) needed in order for zoom level to increase by 1x.
     */
    protected var pixelsPerOneZoomLevel = 80

    protected var pictureCallback: OnPictureTakenListener? = null
    protected var pictureBytesCallback: OnPictureBytesAvailableListener? = null
    protected var turnFailCallback: OnTurnCameraFailListener? = null
    protected var cameraErrorCallback: OnCameraErrorListener? = null
    protected var focusLockedCallback: OnFocusLockedListener? = null
    protected var onFrameCallback: OnFrameListener? = null

    var maximumWidth = 0
    var maximumPreviewWidth = 0

    protected var orientation: Orientation
    protected var currentOrientationDegrees: Int = 0
    protected var orientationListener: Orientation.Listener = object : Orientation.Listener {
        override fun onOrientationChanged(pitch: Float, roll: Float) {
            currentOrientationDegrees = pitchAndRollToDegrees(pitch, roll)
        }
    }

    protected var mPreviewSizeSelected: Size? = null
    protected var mPictureSizeSelected: Size? = null

    init {
        orientation = Orientation(context, 100)
    }

    internal fun getView(): View {
        return mPreview.getView()
    }

    private fun mirrorBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.preScale(-1.0f, 1.0f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun setOnPictureTakenListener(pictureCallback: OnPictureTakenListener) {
        this.pictureCallback = pictureCallback
    }

    fun setOnPictureBytesAvailableListener(bytesCallback: OnPictureBytesAvailableListener) {
        this.pictureBytesCallback = bytesCallback
    }

    fun setOnFocusLockedListener(focusLockedListener: OnFocusLockedListener) {
        this.focusLockedCallback = focusLockedListener
    }

    fun setOnTurnCameraFailListener(turnCameraFailListener: OnTurnCameraFailListener) {
        this.turnFailCallback = turnCameraFailListener
    }

    fun setOnCameraErrorListener(onCameraErrorListener: OnCameraErrorListener) {
        this.cameraErrorCallback = onCameraErrorListener
    }

    fun setOnFrameListener(onFrameListener: OnFrameListener) {
        this.onFrameCallback = onFrameListener
    }

    /**
     * @return {@code true} if the implementation was able to start the camera session.
     */
    abstract fun start(): Boolean

    abstract fun stop()

    abstract fun isCameraOpened(): Boolean

    abstract fun setFacing(facing: Int)

    abstract fun getFacing(): Int

    abstract fun getSupportedAspectRatios(): Set<AspectRatio>

    /**
     * @return `true` if the aspect ratio was changed.
     */
    abstract fun setAspectRatio(ratio: AspectRatio, isInitializing: Boolean): Boolean

    abstract fun getAspectRatio(): AspectRatio

    abstract fun setAutoFocus(autoFocus: Boolean)

    abstract fun getAutoFocus(): Boolean

    abstract fun setFlash(flash: Int)

    abstract fun getFlash(): Int

    abstract fun takePicture()

    abstract fun setDisplayOrientation(displayOrientation: Int)

    /**
     * Different devices have different default orientations,
     * Therefore we need to take into account this value before passing the rotation
     * in the callback.
     */
    abstract fun getCameraDefaultOrientation(): Int

    /**
     * @return `true` if the motionEvent is consumed.
     */
    internal abstract fun zoom(event: MotionEvent): Boolean

    internal abstract fun onPinchFingerUp()

    protected fun getFingerSpacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    protected fun byteArrayToBitmap(data: ByteArray) {
        if (pictureCallback == null) return  //There's no point of wasting resources if there is no callback registered
        AsyncTask.execute {
            val options = BitmapFactory.Options()
            options.inMutable = true
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, options)
            if (getFacing() == FACING_FRONT) {
                if (pictureCallback != null) pictureCallback!!.onPictureTaken(
                    mirrorBitmap(bitmap),
                    getRotationDegrees()
                )
            } else {
                if (pictureCallback != null) pictureCallback!!.onPictureTaken(bitmap, getRotationDegrees())
            }
        }
    }

    fun setPixelsPerOneZoomLevell(pixels: Int) {
        if (pixels <= 0) return
        pixelsPerOneZoomLevel = pixels
    }

    protected fun getRotationDegrees(): Int {
        return -(currentOrientationDegrees + getCameraDefaultOrientation())
    }

    private fun pitchAndRollToDegrees(pitch: Float, roll: Float): Int {
        return if (roll < -135 || roll > 135) {
            180 //Home button on the top
        } else if (roll > 45 && roll <= 135) {
            270 //Home button on the right
        } else if (roll >= -135 && roll < -45) {
            90 //Home button on the left
        } else {
            0 //Portrait
        }
    }

    interface OnPictureTakenListener {
        fun onPictureTaken(bitmap: Bitmap, rotationDegrees: Int)
    }

    interface OnPictureBytesAvailableListener {
        fun onPictureBytesAvailable(bytes: ByteArray, rotationDegrees: Int)
    }

    interface OnTurnCameraFailListener {
        fun onTurnCameraFail(e: Exception)
    }

    interface OnCameraErrorListener {
        fun onCameraError(e: Exception)
    }

    interface OnFocusLockedListener {
        fun onFocusLocked()
    }

    interface OnFrameListener {
        fun onFrame(data: ByteArray, width: Int, height: Int, rotationDegrees: Int)
    }

}