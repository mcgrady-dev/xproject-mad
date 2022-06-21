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
package com.mcgrady.xproject.retromusic.helper

import android.os.Handler
import android.os.Looper
import android.os.Message
import kotlin.math.max

class MusicProgressViewUpdateHelper : Handler {

    private var callback: Callback? = null
    private var intervalPlaying: Int = 0
    private var intervalPaused: Int = 0
    private var firstUpdate = true

    fun start() {
        queueNextRefresh(refreshProgressViews().toLong())
    }

    fun stop() {
        removeMessages(CMD_REFRESH_PROGRESS_VIEWS)
    }

    constructor(callback: Callback) : super(Looper.getMainLooper()) {
        this.callback = callback
        this.intervalPlaying = UPDATE_INTERVAL_PLAYING
        this.intervalPaused = UPDATE_INTERVAL_PAUSED
    }

    constructor(
        callback: Callback,
        intervalPlaying: Int,
        intervalPaused: Int,
    ) : super(Looper.getMainLooper()) {
        this.callback = callback
        this.intervalPlaying = intervalPlaying
        this.intervalPaused = intervalPaused
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        if (msg.what == CMD_REFRESH_PROGRESS_VIEWS) {
            queueNextRefresh(refreshProgressViews().toLong())
        }
    }

    private fun refreshProgressViews(): Int {
        val progressMillis = MusicPlayerRemote.songProgressMillis
        val totalMillis = MusicPlayerRemote.songDurationMillis
        if (totalMillis > 0) {
            firstUpdate = false
            callback?.onUpdateProgressViews(progressMillis, totalMillis)
        }
        if (!MusicPlayerRemote.isPlaying && !firstUpdate) {
            return intervalPaused
        }

        val remainingMillis = intervalPlaying - progressMillis % intervalPlaying

        return max(MIN_INTERVAL, remainingMillis)
    }

    private fun queueNextRefresh(delay: Long) {
        val message = obtainMessage(CMD_REFRESH_PROGRESS_VIEWS)
        removeMessages(CMD_REFRESH_PROGRESS_VIEWS)
        sendMessageDelayed(message, delay)
    }

    interface Callback {
        fun onUpdateProgressViews(progress: Int, total: Int)
    }

    companion object {
        private const val CMD_REFRESH_PROGRESS_VIEWS = 1
        private const val MIN_INTERVAL = 20
        private const val UPDATE_INTERVAL_PLAYING = 500
        private const val UPDATE_INTERVAL_PAUSED = 500
    }
}
