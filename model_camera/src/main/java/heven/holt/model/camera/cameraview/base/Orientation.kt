package heven.holt.model.camera.cameraview.base

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import android.view.WindowManager
import com.blankj.utilcode.util.LogUtils

class Orientation(context: Context, sensorInterval: Int) : SensorEventListener {
    interface Listener {
        fun onOrientationChanged(pitch: Float, roll: Float)
    }

    private var sensorInterval = 50

    private var mWindowManager: WindowManager? = null
    private var mSensorManager: SensorManager? = null

    private var mRotationSensor: Sensor? = null

    private var mLastAccuracy: Int = 0
    private var mListener: Listener? = null

    constructor(activity: Activity, sensorInterval: Int) : this(activity.baseContext, sensorInterval) {
        mWindowManager = activity.window.windowManager
    }


    init {
        mSensorManager = context.getSystemService(Activity.SENSOR_SERVICE) as SensorManager
        if (mSensorManager != null) mRotationSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        this.sensorInterval = sensorInterval
    }

    fun startListening(listener: Listener) {
        if (mListener === listener) {
            return
        }
        mListener = listener
        if (mRotationSensor == null) {
            LogUtils.w("Orientation", "Rotation vector sensor not available; will not provide orientation data.")
            return
        }
        mSensorManager?.registerListener(this, mRotationSensor, sensorInterval)
    }

    fun stopListening() {
        mSensorManager?.unregisterListener(this)
        mListener = null
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        if (mLastAccuracy != accuracy) {
            mLastAccuracy = accuracy
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (mListener == null) {
            return
        }
        if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return
        }
        if (event?.sensor == mRotationSensor) {
            updateOrientation(event?.values)
        }
    }

    private fun updateOrientation(rotationVector: FloatArray?) {
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector)

        val worldAxisForDeviceAxisX: Int
        val worldAxisForDeviceAxisY: Int

        if (mWindowManager != null) {
            // Remap the axes as if the device screen was the instrument panel,
            // and adjust the rotation matrix for the device orientation.
            when (mWindowManager!!.defaultDisplay.rotation) {
                Surface.ROTATION_0 -> {
                    worldAxisForDeviceAxisX = SensorManager.AXIS_X
                    worldAxisForDeviceAxisY = SensorManager.AXIS_Z
                }
                Surface.ROTATION_90 -> {
                    worldAxisForDeviceAxisX = SensorManager.AXIS_Z
                    worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_X
                }
                Surface.ROTATION_180 -> {
                    worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_X
                    worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_Z
                }
                Surface.ROTATION_270 -> {
                    worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_Z
                    worldAxisForDeviceAxisY = SensorManager.AXIS_X
                }
                else -> {
                    worldAxisForDeviceAxisX = SensorManager.AXIS_X
                    worldAxisForDeviceAxisY = SensorManager.AXIS_Z
                }
            }
        } else {
            worldAxisForDeviceAxisX = SensorManager.AXIS_X
            worldAxisForDeviceAxisY = SensorManager.AXIS_Z
        }

        val adjustedRotationMatrix = FloatArray(9)
        SensorManager.remapCoordinateSystem(
            rotationMatrix, worldAxisForDeviceAxisX,
            worldAxisForDeviceAxisY, adjustedRotationMatrix
        )

        // Transform rotation matrix into azimuth/pitch/roll
        val orientation = FloatArray(3)
        SensorManager.getOrientation(adjustedRotationMatrix, orientation)

        // Convert radians to degrees
        val pitch = orientation[1] * -57
        val roll = orientation[2] * -57

        mListener?.onOrientationChanged(pitch, roll)
    }
}