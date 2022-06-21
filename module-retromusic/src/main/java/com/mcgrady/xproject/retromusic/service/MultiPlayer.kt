/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject.retromusic.service

import android.content.Context
import android.media.MediaPlayer
import android.os.PowerManager
import android.util.Log
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.extensions.showToast
import com.mcgrady.xproject.retromusic.extensions.uri
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.service.playback.Playback.PlaybackCallbacks
import com.mcgrady.xproject.retromusic.util.PreferenceUtil.isGapLessPlayback

/**
 * @author Andrew Neal, Karim Abou Zeid (kabouzeid)
 */
class MultiPlayer(context: Context) : LocalPlayback(context) {
    private var mCurrentMediaPlayer = MediaPlayer()
    private var mNextMediaPlayer: MediaPlayer? = null
    override var callbacks: PlaybackCallbacks? = null

    /**
     * @return True if the player is ready to go, false otherwise
     */
    override var isInitialized = false
        private set

    init {
        mCurrentMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
    }

    /**
     * @param song The song object you want to play
     * @return True if the `player` has been prepared and is ready to play, false otherwise
     */
    override fun setDataSource(
        song: Song,
        force: Boolean,
        completion: (success: Boolean) -> Unit,
    ) {
        isInitialized = false
        setDataSourceImpl(mCurrentMediaPlayer, song.uri.toString()) { success ->
            isInitialized = success
            if (isInitialized) {
                setNextDataSource(null)
            }
            completion(isInitialized)
        }
    }

    /**
     * Set the MediaPlayer to start when this MediaPlayer finishes playback.
     *
     * @param path The path of the file, or the http/rtsp URL of the stream you want to play
     */
    override fun setNextDataSource(path: String?) {
        try {
            mCurrentMediaPlayer.setNextMediaPlayer(null)
        } catch (e: IllegalArgumentException) {
            Log.i(TAG, "Next media player is current one, continuing")
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Media player not initialized!")
            return
        }
        if (mNextMediaPlayer != null) {
            mNextMediaPlayer?.release()
            mNextMediaPlayer = null
        }
        if (path == null) {
            return
        }
        if (isGapLessPlayback) {
            mNextMediaPlayer = MediaPlayer()
            mNextMediaPlayer?.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
            mNextMediaPlayer?.audioSessionId = audioSessionId
            setDataSourceImpl(mNextMediaPlayer!!, path) { success ->
                if (success) {
                    try {
                        mCurrentMediaPlayer.setNextMediaPlayer(mNextMediaPlayer)
                    } catch (e: IllegalArgumentException) {
                        Log.e(TAG, "setNextDataSource: setNextMediaPlayer()", e)
                        if (mNextMediaPlayer != null) {
                            mNextMediaPlayer?.release()
                            mNextMediaPlayer = null
                        }
                    } catch (e: IllegalStateException) {
                        Log.e(TAG, "setNextDataSource: setNextMediaPlayer()", e)
                        if (mNextMediaPlayer != null) {
                            mNextMediaPlayer?.release()
                            mNextMediaPlayer = null
                        }
                    }
                } else {
                    if (mNextMediaPlayer != null) {
                        mNextMediaPlayer?.release()
                        mNextMediaPlayer = null
                    }
                }
            }
        }
    }

    /**
     * Starts or resumes playback.
     */
    override fun start(): Boolean {
        super.start()
        return try {
            mCurrentMediaPlayer.start()
            true
        } catch (e: IllegalStateException) {
            false
        }
    }

    /**
     * Resets the MediaPlayer to its uninitialized state.
     */
    override fun stop() {
        super.stop()
        mCurrentMediaPlayer.reset()
        isInitialized = false
    }

    /**
     * Releases resources associated with this MediaPlayer object.
     */
    override fun release() {
        stop()
        mCurrentMediaPlayer.release()
        if (mNextMediaPlayer != null) {
            mNextMediaPlayer?.release()
        }
    }

    /**
     * Pauses playback. Call start() to resume.
     */
    override fun pause(): Boolean {
        super.pause()
        return try {
            mCurrentMediaPlayer.pause()
            true
        } catch (e: IllegalStateException) {
            false
        }
    }

    /**
     * Checks whether the MultiPlayer is playing.
     */
    override val isPlaying: Boolean
        get() = isInitialized && mCurrentMediaPlayer.isPlaying

    /**
     * Gets the duration of the file.
     *
     * @return The duration in milliseconds
     */
    override fun duration(): Int {
        return if (!this.isInitialized) {
            -1
        } else try {
            mCurrentMediaPlayer.duration
        } catch (e: IllegalStateException) {
            -1
        }
    }

    /**
     * Gets the current playback position.
     *
     * @return The current position in milliseconds
     */
    override fun position(): Int {
        return if (!this.isInitialized) {
            -1
        } else try {
            mCurrentMediaPlayer.currentPosition
        } catch (e: IllegalStateException) {
            -1
        }
    }

    /**
     * Gets the current playback position.
     *
     * @param whereto The offset in milliseconds from the start to seek to
     * @return The offset in milliseconds from the start to seek to
     */
    override fun seek(whereto: Int): Int {
        return try {
            mCurrentMediaPlayer.seekTo(whereto)
            whereto
        } catch (e: IllegalStateException) {
            -1
        }
    }

    override fun setVolume(vol: Float): Boolean {
        return try {
            mCurrentMediaPlayer.setVolume(vol, vol)
            true
        } catch (e: IllegalStateException) {
            false
        }
    }

    /**
     * Sets the audio session ID.
     *
     * @param sessionId The audio session ID
     */
    override fun setAudioSessionId(sessionId: Int): Boolean {
        return try {
            mCurrentMediaPlayer.audioSessionId = sessionId
            true
        } catch (e: IllegalArgumentException) {
            false
        } catch (e: IllegalStateException) {
            false
        }
    }

    /**
     * Returns the audio session ID.
     *
     * @return The current audio session ID.
     */
    override val audioSessionId: Int
        get() = mCurrentMediaPlayer.audioSessionId

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        isInitialized = false
        mCurrentMediaPlayer.release()
        mCurrentMediaPlayer = MediaPlayer()
        mCurrentMediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
        context.showToast(R.string.unplayable_file)
        Log.e(TAG, what.toString() + extra)
        return false
    }

    override fun onCompletion(mp: MediaPlayer) {
        if (mp == mCurrentMediaPlayer && mNextMediaPlayer != null) {
            isInitialized = false
            mCurrentMediaPlayer.release()
            mCurrentMediaPlayer = mNextMediaPlayer!!
            isInitialized = true
            mNextMediaPlayer = null
            if (callbacks != null) callbacks?.onTrackWentToNext()
        } else {
            if (callbacks != null) callbacks?.onTrackEnded()
        }
    }

    override fun setCrossFadeDuration(duration: Int) {}

    override fun setPlaybackSpeedPitch(speed: Float, pitch: Float) {
        mCurrentMediaPlayer.setPlaybackSpeedPitch(speed, pitch)
    }

    companion object {
        val TAG: String = MultiPlayer::class.java.simpleName
    }
}
