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
package com.mcgrady.xproject.retromusic.fragments

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import kotlinx.coroutines.*
import kotlin.math.abs

/**
 * @param activity, Activity
 * @param next, if the button is next, if false then it's considered previous
 */
class MusicSeekSkipTouchListener(val activity: FragmentActivity, val next: Boolean) :
    View.OnTouchListener {

    private var job: Job? = null
    private var counter = 0
    private var wasSeeking = false

    private var startX = 0f
    private var startY = 0f

    private val scaledTouchSlop = ViewConfiguration.get(activity).scaledTouchSlop

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                job = activity.lifecycleScope.launch(Dispatchers.Default) {
                    counter = 0
                    while (isActive) {
                        delay(500)
                        wasSeeking = true
                        var seekingDuration = MusicPlayerRemote.songProgressMillis
                        if (next) {
                            seekingDuration += 5000 * (counter.floorDiv(2) + 1)
                        } else {
                            seekingDuration -= 5000 * (counter.floorDiv(2) + 1)
                        }
                        withContext(Dispatchers.Main) {
                            MusicPlayerRemote.seekTo(seekingDuration)
                        }
                        counter += 1
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                job?.cancel()
                val endX = event.x
                val endY = event.y
                if (!wasSeeking && isAClick(startX, endX, startY, endY)) {
                    if (next) {
                        MusicPlayerRemote.playNextSong()
                    } else {
                        MusicPlayerRemote.back()
                    }
                }

                wasSeeking = false
            }
            MotionEvent.ACTION_CANCEL -> {
                job?.cancel()
            }
        }
        return false
    }

    private fun isAClick(startX: Float, endX: Float, startY: Float, endY: Float): Boolean {
        val differenceX = abs(startX - endX)
        val differenceY = abs(startY - endY)
        return !(differenceX > scaledTouchSlop || differenceY > scaledTouchSlop)
    }
}
