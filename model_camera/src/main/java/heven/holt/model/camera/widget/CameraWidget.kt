package heven.holt.model.camera.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import heven.holt.model.camera.R
import heven.holt.model.camera.utils.CameraUtils
import heven.holt.model.camera.utils.SensorControler
import kotlinx.android.synthetic.main.camera_widget_camera.view.*

class CameraWidget(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    var surfaceViewCamera: TextureCamera? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.camera_widget_camera, this, true)
        this.surfaceViewCamera = cameraSurfaceView
    }

    fun openTouchFocus() {
        cameraFocus.visibility = View.VISIBLE
        cameraSurfaceView.touchPointListener = {
            cameraFocus.x = it.x - cameraFocus.width / 2
            cameraFocus.y = it.y - cameraFocus.height / 2
            CameraUtils.setFocusAreas(it.x, it.y, cameraSurfaceView.width, cameraSurfaceView.height)
        }
    }

    fun openAutoFocus() {
        SensorControler.focusCallback = {
            CameraUtils.setFocusAreas(
                (cameraSurfaceView.width / 2).toFloat(),
                cameraSurfaceView.height.toFloat(), cameraSurfaceView.width, cameraSurfaceView.height
            )
        }
    }
}