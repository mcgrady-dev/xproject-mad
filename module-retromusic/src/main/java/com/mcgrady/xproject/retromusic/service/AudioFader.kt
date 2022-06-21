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

import android.animation.Animator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import androidx.core.animation.doOnEnd
import com.mcgrady.xproject.retromusic.service.playback.Playback
import com.mcgrady.xproject.retromusic.util.PreferenceUtil

class AudioFader {
    companion object {

        fun createFadeAnimator(
            fadeInMp: MediaPlayer,
            fadeOutMp: MediaPlayer,
            endAction: (animator: Animator) -> Unit, /* Code to run when Animator Ends*/
        ): Animator? {
            val duration = PreferenceUtil.crossFadeDuration * 1000
            if (duration == 0) {
                return null
            }
            return ValueAnimator.ofFloat(0f, 1f).apply {
                this.duration = duration.toLong()
                addUpdateListener { animation: ValueAnimator ->
                    fadeInMp.setVolume(
                        animation.animatedValue as Float, animation.animatedValue as Float
                    )
                    fadeOutMp.setVolume(
                        1 - animation.animatedValue as Float,
                        1 - animation.animatedValue as Float
                    )
                }
                doOnEnd {
                    endAction(it)
                }
            }
        }

        fun startFadeAnimator(
            playback: Playback,
            fadeIn: Boolean, /* fadeIn -> true  fadeOut -> false*/
            callback: Runnable? = null, /* Code to run when Animator Ends*/
        ) {
            val duration = PreferenceUtil.audioFadeDuration.toLong()
            if (duration == 0L) {
                callback?.run()
                return
            }
            val startValue = if (fadeIn) 0f else 1.0f
            val endValue = if (fadeIn) 1.0f else 0f
            val animator = ValueAnimator.ofFloat(startValue, endValue)
            animator.duration = duration
            animator.addUpdateListener { animation: ValueAnimator ->
                playback.setVolume(animation.animatedValue as Float)
            }
            animator.doOnEnd {
                callback?.run()
            }
            animator.start()
        }
    }
}
