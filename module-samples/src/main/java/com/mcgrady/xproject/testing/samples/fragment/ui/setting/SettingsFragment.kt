package com.mcgrady.xproject.testing.samples.fragment.ui.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.mcgrady.xproject.testing.samples.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}