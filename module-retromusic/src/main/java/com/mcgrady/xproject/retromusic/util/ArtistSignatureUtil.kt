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
package com.mcgrady.xproject.retromusic.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.bumptech.glide.signature.ObjectKey

/** @author Karim Abou Zeid (kabouzeid)
 */
class ArtistSignatureUtil private constructor(context: Context) {
    private val mPreferences: SharedPreferences =
        context.getSharedPreferences(ARTIST_SIGNATURE_PREFS, Context.MODE_PRIVATE)

    fun updateArtistSignature(artistName: String?) {
        mPreferences.edit { putLong(artistName, System.currentTimeMillis()) }
    }

    private fun getArtistSignatureRaw(artistName: String?): Long {
        return mPreferences.getLong(artistName, 0)
    }

    fun getArtistSignature(artistName: String?): ObjectKey {
        return ObjectKey(getArtistSignatureRaw(artistName).toString())
    }

    companion object {
        private const val ARTIST_SIGNATURE_PREFS = "artist_signatures"
        private var INSTANCE: ArtistSignatureUtil? = null
        fun getInstance(context: Context): ArtistSignatureUtil {
            if (INSTANCE == null) {
                INSTANCE = ArtistSignatureUtil(context.applicationContext)
            }
            return INSTANCE!!
        }
    }
}
