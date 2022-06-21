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

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mcgrady.xproject.retromusic.App
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.FragmentMainSettingsBinding
import com.mcgrady.xproject.retromusic.extensions.drawAboveSystemBarsWithPadding
import com.mcgrady.xproject.retromusic.util.NavigationUtil
import com.mcgrady.xproject.theme.ThemeStore

class MainSettingsFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentMainSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onClick(view: View) {
        findNavController().navigate(
            when (view.id) {
                R.id.generalSettings -> R.id.action_mainSettingsFragment_to_themeSettingsFragment
                R.id.audioSettings -> R.id.action_mainSettingsFragment_to_audioSettings
                R.id.personalizeSettings -> R.id.action_mainSettingsFragment_to_personalizeSettingsFragment
                R.id.imageSettings -> R.id.action_mainSettingsFragment_to_imageSettingFragment
                R.id.notificationSettings -> R.id.action_mainSettingsFragment_to_notificationSettingsFragment
                R.id.otherSettings -> R.id.action_mainSettingsFragment_to_otherSettingsFragment
                R.id.aboutSettings -> R.id.action_mainSettingsFragment_to_aboutActivity
                R.id.nowPlayingSettings -> R.id.action_mainSettingsFragment_to_nowPlayingSettingsFragment
                R.id.backup_restore_settings -> R.id.action_mainSettingsFragment_to_backupFragment
                else -> R.id.action_mainSettingsFragment_to_themeSettingsFragment
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.generalSettings.setOnClickListener(this)
        binding.audioSettings.setOnClickListener(this)
        binding.nowPlayingSettings.setOnClickListener(this)
        binding.personalizeSettings.setOnClickListener(this)
        binding.imageSettings.setOnClickListener(this)
        binding.notificationSettings.setOnClickListener(this)
        binding.otherSettings.setOnClickListener(this)
        binding.aboutSettings.setOnClickListener(this)
        binding.backupRestoreSettings.setOnClickListener(this)

        binding.buyProContainer.apply {
            isGone = App.isProVersion()
            setOnClickListener {
                NavigationUtil.goToProVersion(requireContext())
            }
        }
        binding.buyPremium.setOnClickListener {
            NavigationUtil.goToProVersion(requireContext())
        }
        ThemeStore.accentColor(requireContext()).let {
            binding.buyPremium.setTextColor(it)
            binding.diamondIcon.imageTintList = ColorStateList.valueOf(it)
        }

        binding.container.drawAboveSystemBarsWithPadding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
