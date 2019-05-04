package heven.holt.model.camera.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import heven.holt.lib_common.base.BaseActivity
import heven.holt.model.camera.R
import heven.holt.model.camera.cameraview.base.CameraViewImpl
import kotlinx.android.synthetic.main.camera_activity_surface_camera.*

class SurfaceViewCameraActivity : BaseActivity() {
    private var frameIsProcessing = false

    override fun getLayoutResID(): Int = R.layout.camera_activity_surface_camera

    override fun initActivity(savedInstanceState: Bundle?) {
    }

    override fun onResume() {
        super.onResume()

        PermissionUtils.permission(PermissionConstants.CAMERA).callback(object : PermissionUtils.SimpleCallback {
            override fun onGranted() {
                camera_view.start()
                setupCameraCallbacks()
            }

            override fun onDenied() {
                ToastUtils.showShort("相机权限获取失败")
            }
        }).request()
    }

    override fun onPause() {
        super.onPause()
        camera_view.stop()
    }

    private fun setupCameraCallbacks() {
        camera_view.setOnPictureTakenListener(object : CameraViewImpl.OnPictureTakenListener {
            override fun onPictureTaken(bitmap: Bitmap, rotationDegrees: Int) {
//                startSavingPhoto(bitmap, rotationDegrees)
            }
        })
        camera_view.setOnFocusLockedListener(object : CameraViewImpl.OnFocusLockedListener {
            override fun onFocusLocked() {
                playShutterAnimation()
            }
        })
        camera_view.setOnTurnCameraFailListener(object : CameraViewImpl.OnTurnCameraFailListener {
            override fun onTurnCameraFail(e: Exception) {
                Toast.makeText(
                    this@SurfaceViewCameraActivity, "Switch Camera Failed. Does you device has a front camera?",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        camera_view.setOnCameraErrorListener(object : CameraViewImpl.OnCameraErrorListener {
            override fun onCameraError(e: Exception) {
                Toast.makeText(this@SurfaceViewCameraActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        })
        camera_view.setOnFrameListener(object : CameraViewImpl.OnFrameListener {
            override fun onFrame(data: ByteArray, width: Int, height: Int, rotationDegrees: Int) {
                if (frameIsProcessing) return
                frameIsProcessing = true
//                Observable.fromCallable(Callable<Bitmap> { Nv21Image.nv21ToBitmap(rs, data, width, height) })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(object : Observer<Bitmap> {
//                        override fun onSubscribe(d: Disposable) {
//
//                        }
//
//                        override fun onNext(frameBitmap: Bitmap?) {
//                            if (frameBitmap != null) {
//                                Log.i("onFrame", frameBitmap.width.toString() + ", " + frameBitmap.height)
//                            }
//                        }
//
//                        override fun onError(e: Throwable) {
//
//                        }
//
//                        override fun onComplete() {
//                            frameIsProcessing = false
//                        }
//                    })
            }
        })
    }

    private fun playShutterAnimation() {
        shutter_effect.visibility = View.VISIBLE
        shutter_effect.animate().alpha(0f).setDuration(300).setListener(
            object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    shutter_effect.visibility = View.GONE
                    shutter_effect.alpha = 0.8f
                }
            })
    }
}