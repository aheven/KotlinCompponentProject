package heven.holt.model.camera.utils

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils


object SensorControler : SensorEventListener, LifecycleObserver {
    private var mx = 0
    private var my = 0
    private var mz = 0
    private var lastStaticStamp: Long = 0

    private const val STATUS_NONE = 0
    private const val STATUS_STATIC = 1
    private const val STATUS_MOVE = 2
    private const val DELAY_DURATION = 500
    private var status = STATUS_NONE

    private val sensorManager: SensorManager = Utils.getApp().getSystemService(Activity.SENSOR_SERVICE) as SensorManager
    private val sensor: Sensor

    var focusCallback: (() -> Unit)? = null

    init {
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    fun getInstance(lifecycleOwner: LifecycleOwner): SensorControler {
        lifecycleOwner.lifecycle.addObserver(this)
        return this
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        LogUtils.e("onAccuracyChanged")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor == null) return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0].toInt()
            val y = event.values[1].toInt()
            val z = event.values[2].toInt()

            val stamp = System.currentTimeMillis()
            if (status != STATUS_NONE) {
                val px = Math.abs(mx - x)
                val py = Math.abs(my - y)
                val pz = Math.abs(mz - z)
                val value = Math.sqrt((px * px + py * py + pz * pz).toDouble())
                if (value > 1.4) {
                    status = STATUS_MOVE
                } else {
                    if (stamp - lastStaticStamp > DELAY_DURATION) {
                        focusCallback?.invoke()
                    }

                    if (status == STATUS_MOVE) {
                        lastStaticStamp = stamp
                    }
                }
            } else {
                lastStaticStamp = stamp
                status = STATUS_STATIC
            }
            mx = x
            my = y
            mz = z
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        resetParams()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        sensorManager.unregisterListener(this)
    }

    private fun resetParams() {
        status = STATUS_NONE
        mx = 0
        my = 0
        mz = 0
    }
}