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

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import com.mcgrady.xproject.retromusic.AUTO_DOWNLOAD_IMAGES_POLICY
import com.mcgrady.xproject.retromusic.R

/**
 * @author Hemanth S (h4h13).
 */

class ImageSettingFragment : AbsSettingsFragment() {
    override fun invalidateSettings() {
        val autoDownloadImagesPolicy: Preference = findPreference(AUTO_DOWNLOAD_IMAGES_POLICY)!!
        setSummary(autoDownloadImagesPolicy)
        autoDownloadImagesPolicy.setOnPreferenceChangeListener { _, o ->
            setSummary(autoDownloadImagesPolicy, o)
            true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_images)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference: Preference? = findPreference(AUTO_DOWNLOAD_IMAGES_POLICY)
        preference?.let { setSummary(it) }
    }
}
