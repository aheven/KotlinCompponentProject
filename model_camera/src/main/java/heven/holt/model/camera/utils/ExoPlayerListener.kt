package heven.holt.model.camera.utils

/**
 *Time:2019/9/24
 *Author:HevenHolt
 *Description:
 */
interface ExoPlayerListener {
    fun onReady(currentPosition: Long, duration: Long)
    fun onProgress(currentPosition: Long, duration: Long)
    fun onEnd(currentPosition: Long, duration: Long)
}