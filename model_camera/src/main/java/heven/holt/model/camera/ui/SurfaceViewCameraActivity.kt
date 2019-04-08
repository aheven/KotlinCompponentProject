package heven.holt.model.camera.ui

import android.os.Bundle
import android.view.View
import heven.holt.lib_common.base.BaseActivity
import heven.holt.model.camera.R
import heven.holt.model.camera.utils.CameraUtils
import heven.holt.model.camera.utils.SensorControler
import kotlinx.android.synthetic.main.camera_activity_surface_camera.*

class SurfaceViewCameraActivity : BaseActivity() {
    override fun getLayoutResID(): Int = R.layout.camera_activity_surface_camera

    override fun initActivity(savedInstanceState: Bundle?) {
        SensorControler.getInstance(this)
        cameraWidget.openAutoFocus()
    }

    fun clickButton(view: View) {
        when (view.id) {
            R.id.switchCamera -> {
                CameraUtils.switchCamera(1 - CameraUtils.cameraId, cameraWidget.surfaceViewCamera?.surface)
            }
        }
    }
}