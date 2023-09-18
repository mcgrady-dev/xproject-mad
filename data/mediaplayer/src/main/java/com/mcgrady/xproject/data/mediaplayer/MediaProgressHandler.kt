package com.mcgrady.xproject.data.mediaplayer

import android.os.Handler
import android.os.Looper
import androidx.media3.common.C
import androidx.media3.session.MediaBrowser
import timber.log.Timber
import java.util.Timer
import kotlin.math.max

class MediaProgressHandler(
    private var callback: Callback? = null,
    private var intervalPlaying: Long = UPDATE_INTERVAL_PLAYING,
    private var intervalPaused: Long = UPDATE_INTERVAL_PAUSED
) : Handler(Looper.getMainLooper()) {

    private var firstUpdate = true

    fun start() {
        queueNextRefresh(refresh())
    }

    fun stop() {
        removeMessages(CMD_REFRESH_PROGRESS_VIEWS)
    }

    private fun refresh(): Long {
        var progressMillis = 0L
        callback?.mediaBrowser?.let { mediaBrowser ->
            Timber.tag("MediaProgressHandler").d("MediaBrowser: currentPosition=${mediaBrowser.currentPosition} duration=${mediaBrowser.duration}")
//            val currentPosition = callback?.mediaBrowser?.currentPosition ?: 0L
//            Timber.tag("MediaProgressHandler").d("MediaBrowser: currentPosition=$currentPosition duration=${mediaBrowser.duration}")
//            progressMillis = currentPosition / 100
//            if (mediaBrowser.duration != C.TIME_UNSET) {
//                val totalMillis: Long = mediaBrowser.duration
//                firstUpdate = false
//                callback?.onUpdateProgress(progressMillis, totalMillis)
//            }
//            if (!mediaBrowser.isPlaying && !firstUpdate) {
//                return intervalPaused
//            }
        }

//        val remaining = intervalPlaying - progressMillis % intervalPlaying

//        return max(MIN_INTERVAL, remaining)
        return intervalPaused
    }

    override fun handleMessage(msg: android.os.Message) {
        super.handleMessage(msg)
        when (msg.what) {
            CMD_REFRESH_PROGRESS_VIEWS -> {
                queueNextRefresh(refresh())
            }
        }
    }

    private fun queueNextRefresh(delay: Long) {
        val message = obtainMessage(CMD_REFRESH_PROGRESS_VIEWS)
        removeMessages(CMD_REFRESH_PROGRESS_VIEWS)
        sendMessageDelayed(message, delay)
    }

    interface Callback {
        fun onUpdateProgress(progress: Long, total: Long)

        val mediaBrowser: MediaBrowser?
    }

    companion object {
        const val CMD_REFRESH_PROGRESS_VIEWS = 0x100
        private const val MIN_INTERVAL = 20L
        private const val UPDATE_INTERVAL_PLAYING = 500L
        private const val UPDATE_INTERVAL_PAUSED = 1000L
    }
}