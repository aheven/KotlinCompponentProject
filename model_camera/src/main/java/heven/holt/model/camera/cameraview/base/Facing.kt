package heven.holt.model.camera.cameraview.base

import androidx.annotation.IntDef

/**
 * 前后摄像头状态
 */
@IntDef(FACING_BACK, FACING_FRONT)
@Retention(AnnotationRetention.SOURCE)
annotation class Facing
