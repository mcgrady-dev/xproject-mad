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
package com.mcgrady.xproject.retromusic.volume

import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.core.content.getSystemService

class AudioVolumeObserver(private val context: Context) {
    private val mAudioManager: AudioManager =
        context.getSystemService()!!
    private var contentObserver: AudioVolumeContentObserver? = null

    fun register(audioStreamType: Int, listener: OnAudioVolumeChangedListener) {
        val handler = Handler(Looper.getMainLooper())
        // with this handler AudioVolumeContentObserver#onChange()
        //   will be executed in the main thread
        // To execute in another thread you can use a Looper
        // +info: https://stackoverflow.com/a/35261443/904907
        contentObserver = AudioVolumeContentObserver(
            handler,
            mAudioManager,
            audioStreamType,
            listener
        )
        context.contentResolver.registerContentObserver(
            Settings.System.CONTENT_URI,
            true,
            contentObserver!!
        )
    }

    fun unregister() {
        if (contentObserver != null) {
            context.contentResolver.unregisterContentObserver(contentObserver!!)
            contentObserver = null
        }
    }
}
