package heven.holt.model.camera.utils

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Message
import com.blankj.utilcode.util.LogUtils
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import java.lang.ref.WeakReference

/**
 *Time:2019/9/24
 *Author:HevenHolt
 *Description:基于ExoPlayer的音乐播放器
 */
class ExoPlayer private constructor(context: Context) : Player.EventListener {

    companion object {
        var exoPlayer: ExoPlayer? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): ExoPlayer {
            if (null == exoPlayer)
                exoPlayer = ExoPlayer(context)
            return exoPlayer!!
        }
    }

    private val simpleExoPlayer: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context)
    private var exoPlayerListener: ExoPlayerListener? = null
    private val handler = MyHandler(this)

    private var isEnd = false
    private val context: WeakReference<Context>

    fun setExoPlayerListener(exoPlayerListener: ExoPlayerListener) {
        this.exoPlayerListener = exoPlayerListener
    }

    init {
        simpleExoPlayer.addListener(this)
        this.context = WeakReference(context)
    }

    fun playSong(url: String, playWhenReady: Boolean = true) {
        isEnd = false
        val dataSourceFactory = DefaultDataSourceFactory(context.get(), "HevenHolt")
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(url))
        simpleExoPlayer.prepare(mediaSource)
        simpleExoPlayer.playWhenReady = playWhenReady
    }

    fun pause() {
        simpleExoPlayer.playWhenReady = false
    }

    fun resume() {
        simpleExoPlayer.playWhenReady = true
    }

    fun isPlaying() = simpleExoPlayer.playWhenReady

    fun release() {
        simpleExoPlayer.release()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playWhenReady && playbackState == Player.STATE_READY) {
            // Active playback.
            LogUtils.i(
                "HevenHolt",
                "stateReady && playWhenReady，play duration = ${simpleExoPlayer.duration}"
            )
            exoPlayerListener?.onReady(simpleExoPlayer.currentPosition, simpleExoPlayer.duration)
            handler.sendEmptyMessageDelayed(0, 300)
        } else if (playWhenReady && playbackState == Player.STATE_ENDED) {
            // Not playing because playback ended, the player is buffering, stopped or
            // failed. Check playbackState and player.getPlaybackError for details.
            LogUtils.i("playWhenReady but state not ready，play duration = ${simpleExoPlayer.duration}")
            exoPlayerListener?.onEnd(0, simpleExoPlayer.duration)
            isEnd = true
        } else {
            // Paused by app.
            LogUtils.i("paused by app，play duration = ${simpleExoPlayer.duration}")
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        //检查由于网络问题而导致的播放失败问题
        if (error.type == ExoPlaybackException.TYPE_SOURCE) {
            LogUtils.i("error ")
            if (error.sourceException is HttpDataSource.HttpDataSourceException) {
                val httpError = error.sourceException as HttpDataSource.HttpDataSourceException
                val requestDataSpec = httpError.dataSpec
                if (httpError is HttpDataSource.InvalidResponseCodeException) {
                    // Cast to InvalidResponseCodeException and retrieve the response code,
                    // message and headers.
                } else {
                    // Try calling httpError.getCause() to retrieve the underlying cause,
                    // although note that it may be null.
                }
            }
        }
    }

    private fun queryProgress() {
        if (simpleExoPlayer.currentPosition >= simpleExoPlayer.duration) {
            exoPlayerListener?.onEnd(0, simpleExoPlayer.duration)
        } else
            exoPlayerListener?.onProgress(simpleExoPlayer.currentPosition, simpleExoPlayer.duration)
    }

    fun setVolume(volume: Int) {
        simpleExoPlayer.volume = volume * 0.01f
    }

    private class MyHandler(exoPlayer: ExoPlayer) : Handler() {
        private val parent = WeakReference(exoPlayer)

        override fun handleMessage(msg: Message) {
            if (parent.get() == null) {
                return
            }
            val exoPlayer = parent.get()
            when (msg.what) {
                0 -> {
                    exoPlayer?.queryProgress()
                    if (exoPlayer?.isPlaying() == true && !exoPlayer.isEnd) {
                        sendEmptyMessageDelayed(0, 300)
                    }
                }
                else -> {
                }
            }
        }
    }
}