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

import android.os.Handler
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.PLAY_STATE_CHANGED

class ThrottledSeekHandler(
    private val musicService: MusicService,
    private val handler: Handler
) : Runnable {

    fun notifySeek() {
        musicService.updateMediaSessionPlaybackState()
        handler.removeCallbacks(this)
        handler.postDelayed(this, THROTTLE)
    }

    override fun run() {
        musicService.savePositionInTrack()
        musicService.sendPublicIntent(PLAY_STATE_CHANGED) // for musixmatch synced lyrics
    }

    companion object {
        // milliseconds to throttle before calling run() to aggregate events
        private const val THROTTLE: Long = 500
    }
}
