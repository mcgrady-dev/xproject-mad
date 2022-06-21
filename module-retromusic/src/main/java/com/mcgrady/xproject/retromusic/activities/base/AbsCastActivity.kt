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
package com.mcgrady.xproject.retromusic.activities.base

import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.mcgrady.xproject.retromusic.cast.RetroSessionManagerListener
import com.mcgrady.xproject.retromusic.cast.RetroWebServer
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import org.koin.android.ext.android.inject

abstract class AbsCastActivity : AbsSlidingMusicPanelActivity() {

    private var mCastSession: CastSession? = null
    private val sessionManager by lazy {
        CastContext.getSharedInstance(this).sessionManager
    }

    private val webServer: RetroWebServer by inject()

    private val playServicesAvailable: Boolean by lazy {
        try {
            GoogleApiAvailability
                .getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS
        } catch (e: Exception) {
            false
        }
    }

    private val sessionManagerListener by lazy {
        object : RetroSessionManagerListener {
            override fun onSessionStarting(castSession: CastSession) {
                webServer.start()
            }

            override fun onSessionStarted(castSession: CastSession, p1: String) {
                invalidateOptionsMenu()
                mCastSession = castSession
                MusicPlayerRemote.switchToRemotePlayback(castSession)
            }

            override fun onSessionEnded(castSession: CastSession, p1: Int) {
                invalidateOptionsMenu()
                if (mCastSession == castSession) {
                    mCastSession = null
                }
                MusicPlayerRemote.switchToLocalPlayback()
                webServer.stop()
            }

            override fun onSessionResumed(castSession: CastSession, p1: Boolean) {
                invalidateOptionsMenu()
                mCastSession = castSession
                webServer.start()
                MusicPlayerRemote.switchToRemotePlayback(castSession)
            }

            override fun onSessionSuspended(castSession: CastSession, p1: Int) {
                invalidateOptionsMenu()
                if (mCastSession == castSession) {
                    mCastSession = null
                }
                MusicPlayerRemote.switchToLocalPlayback()
                webServer.stop()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (playServicesAvailable) {
            sessionManager.addSessionManagerListener(
                sessionManagerListener,
                CastSession::class.java
            )
            if (mCastSession == null) {
                mCastSession = sessionManager.currentCastSession
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (playServicesAvailable) {
            sessionManager.removeSessionManagerListener(
                sessionManagerListener,
                CastSession::class.java
            )
            mCastSession = null
        }
    }
}
