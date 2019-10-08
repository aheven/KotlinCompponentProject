package heven.holt.model.camera.ui

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.TimeUtils
import com.google.android.exoplayer2.Player
import heven.holt.lib_common.base.BaseActivity
import heven.holt.model.camera.R
import heven.holt.model.camera.utils.ExoPlayer
import heven.holt.model.camera.utils.ExoPlayerListener
import kotlinx.android.synthetic.main.camera_activity_exo.*

/**
 *Time:2019/9/23
 *Author:HevenHolt
 *Description:
 */
class ExoPlayerActivity : BaseActivity(), Player.EventListener, ExoPlayerListener {
    private val songs = listOf(
        "http://music.163.com/song/media/outer/url?id=317151.mp3",
        "http://music.163.com/song/media/outer/url?id=281951.mp3",
        "http://music.163.com/song/media/outer/url?id=281953.mp3",
        "http://music.163.com/song/media/outer/url?id=281954.mp3",
        "http://music.163.com/song/media/outer/url?id=281955.mp3"
    )

    override fun getLayoutResID(): Int = R.layout.camera_activity_exo

    override fun initActivity(savedInstanceState: Bundle?) {
        ExoPlayer.getInstance(this).setExoPlayerListener(this)
    }

    private var currentIndex = 0

    fun clickButton(view: View) {
        when (view.id) {
            R.id.next -> {
                if (currentIndex > songs.lastIndex) {
                    currentIndex = 0
                }
                ExoPlayer.getInstance(this).playSong(songs[currentIndex++])
            }
            R.id.pause -> {
                ExoPlayer.getInstance(this).pause()
            }
            R.id.resume -> {
                ExoPlayer.getInstance(this).resume()
            }
            R.id.release ->{
                ExoPlayer.getInstance(this).release()
            }
        }
    }

    override fun onReady(currentPosition: Long, duration: Long) {
        onProgress(currentPosition, duration)
    }

    override fun onProgress(currentPosition: Long, duration: Long) {
        val progressP = TimeUtils.millis2String(currentPosition, "mm:ss")
        val total = TimeUtils.millis2String(duration, "mm:ss")
        progress.text = "time : $progressP/$total"
    }

    override fun onEnd(currentPosition: Long, duration: Long) {
        progress.text = "music end"
    }
}