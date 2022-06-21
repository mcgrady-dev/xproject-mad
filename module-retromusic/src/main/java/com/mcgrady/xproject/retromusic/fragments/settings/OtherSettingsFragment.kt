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
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.mcgrady.xproject.retromusic.LANGUAGE_NAME
import com.mcgrady.xproject.retromusic.LAST_ADDED_CUTOFF
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.fragments.LibraryViewModel
import com.mcgrady.xproject.retromusic.fragments.ReloadType.HomeSections
import com.mcgrady.xproject.theme.common.prefs.supportv7.ATEListPreference
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

/**
 * @author Hemanth S (h4h13).
 */

class OtherSettingsFragment : AbsSettingsFragment() {
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    override fun invalidateSettings() {
        val languagePreference: ATEListPreference? = findPreference(LANGUAGE_NAME)
        languagePreference?.setOnPreferenceChangeListener { _, _ ->
            restartActivity()
            return@setOnPreferenceChangeListener true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_advanced)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference: Preference? = findPreference(LAST_ADDED_CUTOFF)
        preference?.setOnPreferenceChangeListener { lastAdded, newValue ->
            setSummary(lastAdded, newValue)
            libraryViewModel.forceReload(HomeSections)
            true
        }
        val languagePreference: Preference? = findPreference(LANGUAGE_NAME)
        languagePreference?.setOnPreferenceChangeListener { prefs, newValue ->
            setSummary(prefs, newValue)
            val code = newValue.toString()
            val manager = SplitInstallManagerFactory.create(requireContext())
            if (code != "auto") {
                // Try to download language resources
                val request =
                    SplitInstallRequest.newBuilder().addLanguage(Locale.forLanguageTag(code))
                        .build()
                manager.startInstall(request)
                    // Recreate the activity on download complete
                    .addOnCompleteListener {
                        restartActivity()
                    }
            } else {
                requireActivity().recreate()
            }
            true
        }
    }
}
