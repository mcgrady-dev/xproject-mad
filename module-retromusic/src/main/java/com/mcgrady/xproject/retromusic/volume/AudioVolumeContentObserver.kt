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

import android.database.ContentObserver
import android.media.AudioManager
import android.net.Uri
import android.os.Handler

class AudioVolumeContentObserver internal constructor(
    handler: Handler,
    audioManager: AudioManager,
    audioStreamType: Int,
    listener: OnAudioVolumeChangedListener
) : ContentObserver(handler) {
    private val mListener: OnAudioVolumeChangedListener?
    private val mAudioManager: AudioManager?
    private val mAudioStreamType: Int
    private var mLastVolume: Float

    /** Depending on the handler this method may be executed on the UI thread  */
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        if (mAudioManager != null && mListener != null) {
            val maxVolume = mAudioManager.getStreamMaxVolume(mAudioStreamType)
            val currentVolume = mAudioManager.getStreamVolume(mAudioStreamType)
            if (currentVolume.toFloat() != mLastVolume) {
                mLastVolume = currentVolume.toFloat()
                mListener.onAudioVolumeChanged(currentVolume, maxVolume)
            }
        }
    }

    init {
        mAudioManager = audioManager
        mAudioStreamType = audioStreamType
        mListener = listener
        mLastVolume = audioManager.getStreamVolume(mAudioStreamType).toFloat()
    }
}
