package heven.holt.model.camera

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import heven.holt.lib_common.base.BaseFragment
import heven.holt.model.camera.ui.SurfaceViewCameraActivity
import heven.holt.model.camera.ui.SurfaceViewCreateActivity
import kotlinx.android.synthetic.main.camera_fragment_main.*

@Route(path = "/camera/fragment")
class CameraFragment : BaseFragment(), View.OnClickListener {
    override fun getLayoutResID(): Int = R.layout.camera_fragment_main

    override fun initFragment(savedInstanceState: Bundle?) {
        createSurfaceView.setOnClickListener(this)
        cameraSurfaceView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.createSurfaceView -> {
                ActivityUtils.startActivity(SurfaceViewCreateActivity::class.java)
            }
            R.id.cameraSurfaceView -> {
                val permissionUtils = PermissionUtils.permission(PermissionConstants.CAMERA)
                permissionUtils.callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        ActivityUtils.startActivity(SurfaceViewCameraActivity::class.java)
                    }

                    override fun onDenied() {
                        showMsgToast("请打开相机权限")
                    }

                })
                permissionUtils.request()
            }
        }
    }
}