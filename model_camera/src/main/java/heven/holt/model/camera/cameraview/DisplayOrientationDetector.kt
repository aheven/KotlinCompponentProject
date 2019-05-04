package heven.holt.model.camera.cameraview

import android.content.Context
import android.util.SparseIntArray
import android.view.Display
import android.view.OrientationEventListener
import android.view.Surface

abstract class DisplayOrientationDetector(context: Context){
    private var mOrientationEventListener: OrientationEventListener

    /** Mapping from Surface.Rotation_n to degrees.  */
    val DISPLAY_ORIENTATIONS = SparseIntArray().apply {
        put(Surface.ROTATION_0, 0)
        put(Surface.ROTATION_90, 90)
        put(Surface.ROTATION_180, 180)
        put(Surface.ROTATION_270, 270)
    }

    var mDisplay: Display? = null

    private var mLastKnownDisplayOrientation = 0

    init {
        mOrientationEventListener = object : OrientationEventListener(context) {

            /** This is either Surface.Rotation_0, _90, _180, _270, or -1 (invalid).  */
            private var mLastKnownRotation = -1

            override fun onOrientationChanged(orientation: Int) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN || mDisplay == null) {
                    return
                }
                val rotation = mDisplay!!.rotation
                if (mLastKnownRotation != rotation) {
                    mLastKnownRotation = rotation
                    dispatchOnDisplayOrientationChanged(DISPLAY_ORIENTATIONS.get(rotation))
                }
            }
        }
    }

    fun enable(display: Display) {
        mDisplay = display
        mOrientationEventListener.enable()
        // Immediately dispatch the first callback
        dispatchOnDisplayOrientationChanged(DISPLAY_ORIENTATIONS.get(display.rotation))
    }

    fun disable() {
        mOrientationEventListener.disable()
        mDisplay = null
    }

    fun getLastKnownDisplayOrientation(): Int {
        return mLastKnownDisplayOrientation
    }

    fun dispatchOnDisplayOrientationChanged(displayOrientation: Int) {
        mLastKnownDisplayOrientation = displayOrientation
        onDisplayOrientationChanged(displayOrientation)
    }

    /**
     * Called when display orientation is changed.
     *
     * @param displayOrientation One of 0, 90, 180, and 270.
     */
    abstract fun onDisplayOrientationChanged(displayOrientation: Int)
}