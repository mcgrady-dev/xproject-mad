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
package com.mcgrady.xproject.retromusic.fragments.settings

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.media.audiofx.AudioEffect
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.preference.Preference
import com.mcgrady.xproject.retromusic.BLUETOOTH_PLAYBACK
import com.mcgrady.xproject.retromusic.EQUALIZER
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.activities.base.AbsBaseActivity.Companion.BLUETOOTH_PERMISSION_REQUEST
import com.mcgrady.xproject.retromusic.util.NavigationUtil
import com.mcgrady.xproject.theme.util.VersionUtils

/**
 * @author Hemanth S (h4h13).
 */

class AudioSettings : AbsSettingsFragment() {
    override fun invalidateSettings() {
        val eqPreference: Preference? = findPreference(EQUALIZER)
        if (!hasEqualizer()) {
            eqPreference?.isEnabled = false
            eqPreference?.summary = resources.getString(R.string.no_equalizer)
        } else {
            eqPreference?.isEnabled = true
        }
        eqPreference?.setOnPreferenceClickListener {
            NavigationUtil.openEqualizer(requireActivity())
            true
        }
        val bluetoothPreference: Preference? = findPreference(BLUETOOTH_PLAYBACK)
        if (VersionUtils.hasS()) {
            bluetoothPreference?.setOnPreferenceChangeListener { _, newValue ->
                if (newValue as Boolean) {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            BLUETOOTH_CONNECT
                        ) != PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(
                                BLUETOOTH_CONNECT
                            ),
                            BLUETOOTH_PERMISSION_REQUEST
                        )
                    }
                }
                return@setOnPreferenceChangeListener true
            }
        }
    }

    private fun hasEqualizer(): Boolean {
        val effects = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)

        val pm = requireActivity().packageManager
        val ri = pm.resolveActivity(effects, 0)
        return ri != null
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_audio)
    }
}
