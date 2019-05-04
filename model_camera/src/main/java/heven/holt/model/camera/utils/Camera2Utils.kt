package heven.holt.model.camera.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Build
import android.util.Size
import android.view.Surface
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.LogUtils
import java.util.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
object Camera2Utils : CameraDevice.StateCallback() {
    private var cameraDevice: CameraDevice? = null
    private var outputTarget: Surface? = null
    var mPreviewSize: Size? = null

    @SuppressLint("MissingPermission")
    fun getInstance(context: Context, outputTarget: Surface, width: Int, height: Int) {
        this.outputTarget = outputTarget
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        var cameraId: String? = null
        manager.cameraIdList.forEach {
            //摄像头相关参数
            val cameraCharacteristics = manager.getCameraCharacteristics(it)
            val map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture::class.java), height, width)
            val facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)
            if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                //前置摄像头
                cameraId = it
                return@forEach
            } else if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                //后置摄像头
                cameraId = it
                return@forEach
            }
        }
        if (cameraId == null) {
            LogUtils.e("this phone can not found camera.")
            return
        }
        manager.openCamera(cameraId!!, this, null)
    }

    override fun onOpened(camera: CameraDevice) {
        cameraDevice = camera
        outputTarget?.let { createCameraPreviewSession(it) }
    }

    private fun createCameraPreviewSession(outputTarget: Surface) {
        if (cameraDevice == null) {
            LogUtils.e("cameraDevice is null")
            return
        }
        val builder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        builder.addTarget(outputTarget)
        //创建一个CameraCaptureSession来进行相机预览。
        cameraDevice!!.createCaptureSession(mutableListOf(outputTarget), object : CameraCaptureSession.StateCallback() {
            override fun onConfigureFailed(session: CameraCaptureSession) {
                LogUtils.e("onConfigureFailed 开启预览失败")
            }

            override fun onConfigured(session: CameraCaptureSession) {
                if (null == cameraDevice) return
                //自动对焦
                builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
                // 闪光灯
//                setAutoFlash(mPreviewRequestBuilder)
                //开启相机预览并添加事件
                val captureRequest = builder.build()
                session.setRepeatingRequest(captureRequest, null, null)
            }

        }, null)
    }

    override fun onDisconnected(camera: CameraDevice) {
        camera.close()
        cameraDevice = null
    }

    override fun onError(camera: CameraDevice, error: Int) {
        camera.close()
        cameraDevice = null
    }

    fun releaseCamera() {
        if (cameraDevice == null) return
        cameraDevice!!.close()
        cameraDevice = null
    }

    private fun getOptimalSize(sizeMap: Array<Size>, width: Int, height: Int): Size {
        val sizeList = mutableListOf<Size>()
        sizeMap.forEach {
            if (width > height) {
                if (it.width > width && it.height > height) {
                    sizeList.add(it)
                }
            } else {
                if (it.width > height && it.height > width) {
                    sizeList.add(it)
                }
            }
        }
        if (sizeList.isNotEmpty()) {
            return Collections.min(sizeList) { lhs, rhs ->
                val compare = lhs.width * lhs.height - rhs.width * rhs.height
                if (compare > 0) 1 else if (compare < 0) -1 else 0
            }
        }
        return sizeList.first()
    }
}