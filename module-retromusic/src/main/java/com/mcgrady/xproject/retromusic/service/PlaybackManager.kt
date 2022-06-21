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
import android.content.Intent
import android.media.audiofx.AudioEffect
import com.google.android.gms.cast.framework.CastSession
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.service.playback.Playback
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.retromusic.util.PreferenceUtil.playbackPitch
import com.mcgrady.xproject.retromusic.util.PreferenceUtil.playbackSpeed

class PlaybackManager(val context: Context) {

    var playback: Playback? = null
    private var playbackLocation = PlaybackLocation.LOCAL

    val isLocalPlayback get() = playbackLocation == PlaybackLocation.LOCAL

    val audioSessionId: Int
        get() = if (playback != null) {
            playback!!.audioSessionId
        } else 0

    val songDurationMillis: Int
        get() = if (playback != null) {
            playback!!.duration()
        } else -1

    val songProgressMillis: Int
        get() = if (playback != null) {
            playback!!.position()
        } else -1

    val isPlaying: Boolean
        get() = playback != null && playback!!.isPlaying

    private val shouldSetSpeed: Boolean
        get() = !(playbackSpeed == 1f && playbackPitch == 1f)

    init {
        playback = createLocalPlayback()
    }

    fun setCallbacks(callbacks: Playback.PlaybackCallbacks) {
        playback?.callbacks = callbacks
    }

    fun play(onNotInitialized: () -> Unit) {
        if (playback != null && !playback!!.isPlaying) {
            if (!playback!!.isInitialized) {
                onNotInitialized()
            } else {
                openAudioEffectSession()
                if (playbackLocation == PlaybackLocation.LOCAL) {
                    if (playback is CrossFadePlayer) {
                        if (!(playback as CrossFadePlayer).isCrossFading) {
                            AudioFader.startFadeAnimator(playback!!, true)
                        }
                    } else {
                        AudioFader.startFadeAnimator(playback!!, true)
                    }
                }
                if (shouldSetSpeed) {
                    playback?.setPlaybackSpeedPitch(playbackSpeed, playbackPitch)
                }
                playback?.start()
            }
        }
    }

    fun pause(force: Boolean, onPause: () -> Unit) {
        if (playback != null && playback!!.isPlaying) {
            if (force) {
                playback?.pause()
                closeAudioEffectSession()
                onPause()
            } else {
                AudioFader.startFadeAnimator(playback!!, false) {
                    // Code to run when Animator Ends
                    playback?.pause()
                    closeAudioEffectSession()
                    onPause()
                }
            }
        }
    }

    fun seek(millis: Int): Int = playback!!.seek(millis)

    fun setDataSource(
        song: Song,
        force: Boolean,
        completion: (success: Boolean) -> Unit,
    ) {
        playback?.setDataSource(song, force, completion)
    }

    fun setNextDataSource(trackUri: String) {
        playback?.setNextDataSource(trackUri)
    }

    fun setCrossFadeDuration(duration: Int) {
        playback?.setCrossFadeDuration(duration)
    }

    /**
     * @param crossFadeDuration CrossFade duration
     * @return Whether switched playback
     */
    fun maybeSwitchToCrossFade(crossFadeDuration: Int): Boolean {
        /* Switch to MultiPlayer if CrossFade duration is 0 and
                Playback is not an instance of MultiPlayer */
        if (playback !is MultiPlayer && crossFadeDuration == 0) {
            if (playback != null) {
                playback?.release()
            }
            playback = null
            playback = MultiPlayer(context)
            return true
        } else if (playback !is CrossFadePlayer && crossFadeDuration > 0) {
            if (playback != null) {
                playback?.release()
            }
            playback = null
            playback = CrossFadePlayer(context)
            return true
        }
        return false
    }

    fun release() {
        playback?.release()
        playback = null
        closeAudioEffectSession()
    }

    private fun openAudioEffectSession() {
        val intent = Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION)
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId)
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.packageName)
        intent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
        context.sendBroadcast(intent)
    }

    private fun closeAudioEffectSession() {
        val audioEffectsIntent = Intent(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION)
        if (playback != null) {
            audioEffectsIntent.putExtra(
                AudioEffect.EXTRA_AUDIO_SESSION,
                playback!!.audioSessionId
            )
        }
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.packageName)
        context.sendBroadcast(audioEffectsIntent)
    }

    fun switchToLocalPlayback(onChange: (wasPlaying: Boolean, progress: Int) -> Unit) {
        playbackLocation = PlaybackLocation.LOCAL
        switchToPlayback(createLocalPlayback(), onChange)
    }

    fun switchToRemotePlayback(
        castSession: CastSession,
        onChange: (wasPlaying: Boolean, progress: Int) -> Unit,
    ) {
        playbackLocation = PlaybackLocation.REMOTE
        switchToPlayback(CastPlayer(castSession), onChange)
    }

    private fun switchToPlayback(
        playback: Playback,
        onChange: (wasPlaying: Boolean, progress: Int) -> Unit,
    ) {
        val oldPlayback = this.playback
        val wasPlaying: Boolean = oldPlayback?.isPlaying == true
        val progress: Int = oldPlayback?.position() ?: 0
        this.playback = playback
        oldPlayback?.stop()
        onChange(wasPlaying, progress)
    }

    private fun createLocalPlayback(): Playback {
        // Set MultiPlayer when crossfade duration is 0 i.e. off
        return if (PreferenceUtil.crossFadeDuration == 0) {
            MultiPlayer(context)
        } else {
            CrossFadePlayer(context)
        }
    }

    fun setPlaybackSpeedPitch(playbackSpeed: Float, playbackPitch: Float) {
        playback?.setPlaybackSpeedPitch(playbackSpeed, playbackPitch)
    }
}

enum class PlaybackLocation {
    LOCAL,
    REMOTE
}
