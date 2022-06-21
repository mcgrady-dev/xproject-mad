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
package com.mcgrady.xproject.retromusic.service.playback

import com.mcgrady.xproject.retromusic.model.Song

interface Playback {

    val isInitialized: Boolean

    val isPlaying: Boolean

    val audioSessionId: Int

    fun setDataSource(
        song: Song,
        force: Boolean,
        completion: (success: Boolean) -> Unit,
    )

    fun setNextDataSource(path: String?)

    var callbacks: PlaybackCallbacks?

    fun start(): Boolean

    fun stop()

    fun release()

    fun pause(): Boolean

    fun duration(): Int

    fun position(): Int

    fun seek(whereto: Int): Int

    fun setVolume(vol: Float): Boolean

    fun setAudioSessionId(sessionId: Int): Boolean

    fun setCrossFadeDuration(duration: Int)

    fun setPlaybackSpeedPitch(speed: Float, pitch: Float)

    interface PlaybackCallbacks {
        fun onTrackWentToNext()

        fun onTrackEnded()

        fun onTrackEndedWithCrossfade()

        fun onPlayStateChanged()
    }
}
