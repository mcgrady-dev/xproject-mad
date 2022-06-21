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
package com.mcgrady.xproject.retromusic.fragments.backup

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.ActivityRestoreBinding
import com.mcgrady.xproject.retromusic.extensions.accentColor
import com.mcgrady.xproject.retromusic.extensions.accentOutlineColor
import com.mcgrady.xproject.retromusic.extensions.addAccentColor
import com.mcgrady.xproject.retromusic.helper.BackupContent
import com.mcgrady.xproject.retromusic.helper.BackupContent.*
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.retromusic.util.theme.getNightMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestoreActivity : AppCompatActivity() {

    lateinit var binding: ActivityRestoreBinding
    private val backupViewModel: BackupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityRestoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWidth()
        val backupUri = intent?.data
        binding.backupName.setText(getFileName(backupUri))
        binding.cancelButton.accentOutlineColor()
        binding.cancelButton.setOnClickListener {
            finish()
        }
        binding.restoreButton.accentColor()
        binding.checkArtistImages.addAccentColor()
        binding.checkPlaylists.addAccentColor()
        binding.checkSettings.addAccentColor()
        binding.checkUserImages.addAccentColor()
        binding.restoreButton.setOnClickListener {
            val backupContents = mutableListOf<BackupContent>()
            if (binding.checkPlaylists.isChecked) backupContents.add(PLAYLISTS)
            if (binding.checkArtistImages.isChecked) backupContents.add(CUSTOM_ARTIST_IMAGES)
            if (binding.checkSettings.isChecked) backupContents.add(SETTINGS)
            if (binding.checkUserImages.isChecked) backupContents.add(USER_IMAGES)
            lifecycleScope.launch(Dispatchers.IO) {
                if (backupUri != null) {
                    contentResolver.openInputStream(backupUri)?.use {
                        backupViewModel.restoreBackup(this@RestoreActivity, it, backupContents)
                    }
                }
            }
        }
    }

    private fun updateTheme() {
        AppCompatDelegate.setDefaultNightMode(getNightMode())

        // Apply dynamic colors to activity if enabled
        if (PreferenceUtil.materialYou) {
            DynamicColors.applyToActivityIfAvailable(
                this,
                DynamicColorsOptions.Builder()
                    .setThemeOverlay(R.style.ThemeOverlay_Material3_DynamicColors_DayNight)
                    .build()
            )
        }
    }

    private fun getFileName(uri: Uri?): String? {
        when (uri?.scheme) {
            ContentResolver.SCHEME_FILE -> {
                return uri.lastPathSegment
            }
            ContentResolver.SCHEME_CONTENT -> {
                val proj = arrayOf(MediaStore.Files.FileColumns.DISPLAY_NAME)
                contentResolver.query(
                    uri, proj, null, null, null
                )?.use { cursor ->
                    if (cursor.count != 0) {
                        val columnIndex: Int =
                            cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                        cursor.moveToFirst()
                        return cursor.getString(columnIndex)
                    }
                }
            }
        }
        return "Backup"
    }

    private fun setWidth() {
        val width = resources.displayMetrics.widthPixels * 0.8
        binding.root.updateLayoutParams<ViewGroup.LayoutParams> { this.width = width.toInt() }
    }
}
