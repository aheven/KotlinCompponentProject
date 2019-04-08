@file:Suppress("DEPRECATION")

package heven.holt.model.camera.utils

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import heven.holt.model.camera.utils.CameraForceUtils.calculateTapArea

object CameraUtils {
    // 相机默认宽高，相机的宽度和高度跟屏幕坐标不一样，手机屏幕的宽度和高度是反过来的。
    private const val DEFAULT_WIDTH = 1280
    private const val DEFAULT_HEIGHT = 720
    const val DESIRED_PREVIEW_FPS = 30

    private var camera: Camera? = null
    var cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT
    private var mCameraPreviewFps = 0
    private var mOrientation = 0

    fun openFrontalCamera(expectFps: Int) {
        if (camera != null)
            throw RuntimeException("camera already initialized.")
        calculateCameraPreviewOrientation()
        val info = Camera.CameraInfo()
        val numCameras = Camera.getNumberOfCameras()
        (0 until numCameras).forEach {
            Camera.getCameraInfo(it, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camera = Camera.open(it)
                cameraId = info.facing
                return@forEach
            }
        }
        //如果没有前置摄像头，则打开默认的后置摄像头
        if (camera == null) {
            camera = Camera.open()
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK
        }
        //没有摄像头，则抛出异常
        if (camera == null)
            throw RuntimeException("Unable to open camera")

        val parameters = camera?.parameters
        mCameraPreviewFps = chooseFixedPreviewFps(parameters, expectFps)
        parameters?.setRecordingHint(true)
        setPreviewSize(camera!!, DEFAULT_WIDTH, DEFAULT_HEIGHT)
        setPictureSize(camera!!, DEFAULT_WIDTH, DEFAULT_HEIGHT)
        camera?.setDisplayOrientation(mOrientation)
    }

    private fun setPictureSize(camera: Camera, expectWidth: Int, expectHeight: Int) {
        val parameters = camera.parameters
        val size = calculatePerfectSize(parameters.supportedPictureSizes, expectWidth, expectHeight)
        parameters.setPictureSize(size.width, size.height)
        camera.parameters = parameters
    }

    /**
     * 设置预览大小
     */
    private fun setPreviewSize(camera: Camera, expectWidth: Int, expectHeight: Int) {
        val parameters = camera.parameters
        val size = calculatePerfectSize(parameters.supportedPreviewSizes, expectWidth, expectHeight)
        parameters.setPreviewSize(size.width, size.height)
        camera.parameters = parameters
    }

    /**
     * 计算匹配的CameraSize
     */
    private fun calculatePerfectSize(sizes: List<Camera.Size>, expectWidth: Int, expectHeight: Int): Camera.Size {
        //根据宽度排序
        val sortedBy = sizes.sortedBy { it.width }
        var result = sortedBy[0]
        //判断存在宽或高相等的Size
        var widthOrHeight = false
        for (size in sortedBy) {
            // 如果宽高相等，则直接返回
            if (size.width == expectWidth && size.height == expectHeight) {
                result = size
                return result
            }
            // 仅仅是宽度相等，计算高度最接近的size
            if (size.width == expectWidth) {
                widthOrHeight = true
                if (Math.abs(result.height - expectHeight)
                    > Math.abs(size.height - expectHeight)
                ) {
                    result = size
                }
            }
            // 高度相等，则计算宽度最接近的Size
            else if (size.height == expectHeight) {
                widthOrHeight = true
                if (Math.abs(result.width - expectWidth)
                    > Math.abs(size.width - expectWidth)
                ) {
                    result = size
                }
            }
            // 如果之前的查找不存在宽或高相等的情况，则计算宽度和高度都最接近的期望值的Size
            else if (!widthOrHeight) {
                if (Math.abs(result.width - expectWidth)
                    > Math.abs(size.width - expectWidth)
                    && Math.abs(result.height - expectHeight)
                    > Math.abs(size.height - expectHeight)
                ) {
                    result = size
                }
            }
        }
        return result
    }

    /**
     * 选择合适的fps
     */
    private fun chooseFixedPreviewFps(parameters: Camera.Parameters?, expectFps: Int): Int {
        val previewFpsRange = parameters?.supportedPreviewFpsRange ?: return 0
        for (entry in previewFpsRange) {
            if (entry[0] == entry[1] && entry[0] == expectFps) {
                parameters.setPreviewFpsRange(entry[0], entry[1])
                return entry[0]
            }
        }
        val temp = IntArray(2)
        val guess: Int
        parameters.getPreviewFpsRange(temp)
        guess = if (temp[0] == temp[1]) {
            temp[0]
        } else {
            temp[1] / 2
        }
        return guess
    }

    /**
     * 开始预览
     */
    fun startPreviewDisplay(holder: SurfaceHolder?) {
        if (camera == null) {
            throw IllegalStateException("Camera must be set when start preview")
        }
        camera?.setPreviewDisplay(holder)
        camera?.startPreview()
    }

    fun startPreviewTexture(surface: SurfaceTexture?) {
        if (camera == null) {
            throw IllegalStateException("Camera must be set when start preview")
        }
        camera?.setPreviewTexture(surface)
        camera?.startPreview()
    }

    /**
     * 设置预览角度，setDisplayOrientation本身只能改变预览的角度
     * previewFrameCallback以及拍摄出来的照片是不会发生改变的，拍摄出来的照片角度依旧不正常的拍摄的照片需要自行处理
     */
    private fun calculateCameraPreviewOrientation() {
        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, cameraInfo)
        val rotation = ActivityUtils.getTopActivity().windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mOrientation = (cameraInfo.orientation + degrees) % 360
            mOrientation = (360 - mOrientation) % 360
        } else {
            mOrientation = (cameraInfo.orientation - degrees + 360) % 360
        }
    }

    /**
     * 根据cameraId打开相机
     */
    private fun openCamera(cameraId: Int, expectFps: Int) {
        if (camera != null) {
            throw RuntimeException("camera already initialized!")
        }
        camera = Camera.open(cameraId)
        if (camera == null) {
            throw RuntimeException("Unable to open camera")
        }
        this.cameraId = cameraId
        val parameters = camera!!.parameters
        mCameraPreviewFps = chooseFixedPreviewFps(parameters, expectFps * 1000)
        parameters.setRecordingHint(true)
        camera?.parameters = parameters
        setPreviewSize(camera!!, DEFAULT_WIDTH, DEFAULT_HEIGHT)
        setPictureSize(camera!!, DEFAULT_WIDTH, DEFAULT_HEIGHT)
        camera?.setDisplayOrientation(mOrientation)
    }

    /**
     * 设置触摸聚焦区域
     */
    fun setFocusAreas(x: Float, y: Float, viewWidth: Int, viewHeight: Int) {
        val calculateTapArea = calculateTapArea(x, y, 1, viewWidth, viewHeight)

        if (camera == null) {
            LogUtils.e("camera can not be null")
            return
        }
        camera!!.cancelAutoFocus()
        val parameters = camera!!.parameters
        if (parameters.maxNumFocusAreas > 0) {
            val focusAreas = ArrayList<Camera.Area>()
            focusAreas.add(Camera.Area(calculateTapArea, 600)) // set weight to 60%
            parameters.focusAreas = focusAreas
            parameters.meteringAreas = focusAreas
        } else {
            LogUtils.e("focus areas not supported")
            return
        }
        val currentFocusMode = parameters.focusMode
        parameters.focusMode = Camera.Parameters.FOCUS_MODE_MACRO

        try {
            camera?.parameters = parameters
        } catch (e: java.lang.RuntimeException) {

        }

        camera?.autoFocus { _, camera ->
            val params = camera.parameters
            params.focusMode = currentFocusMode
            camera.parameters = params
        }
    }

    /**
     * 切换相机
     */
    fun switchCamera(cameraId: Int, holder: SurfaceHolder?) {
        if (this.cameraId == cameraId) return
        this.cameraId = cameraId
        releaseCamera()
        openCamera(cameraId, mCameraPreviewFps)
        startPreviewDisplay(holder)
        calculateCameraPreviewOrientation()
    }

    fun switchCamera(cameraId: Int, surface: SurfaceTexture?) {
        if (this.cameraId == cameraId) return
        this.cameraId = cameraId
        releaseCamera()
        openCamera(cameraId, mCameraPreviewFps)
        startPreviewTexture(surface)
        calculateCameraPreviewOrientation()
    }

    /**
     * 释放相机
     */
    fun releaseCamera() {
        if (camera != null) {
            camera!!.stopPreview()
            camera!!.release()
            camera = null
        }
    }
}