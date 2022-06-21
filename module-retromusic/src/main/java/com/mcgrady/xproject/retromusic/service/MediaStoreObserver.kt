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

import android.database.ContentObserver
import android.os.Handler

class MediaStoreObserver(
    private val musicService: MusicService,
    private val mHandler: Handler
) : ContentObserver(mHandler), Runnable {

    override fun onChange(selfChange: Boolean) {
        // if a change is detected, remove any scheduled callback
        // then post a new one. This is intended to prevent closely
        // spaced events from generating multiple refresh calls
        mHandler.removeCallbacks(this)
        mHandler.postDelayed(this, REFRESH_DELAY)
    }

    override fun run() {
        // actually call refresh when the delayed callback fires
        // do not send a sticky broadcast here
        musicService.handleAndSendChangeInternal(MusicService.MEDIA_STORE_CHANGED)
    }

    companion object {
        // milliseconds to delay before calling refresh to aggregate events
        private const val REFRESH_DELAY: Long = 500
    }
}
