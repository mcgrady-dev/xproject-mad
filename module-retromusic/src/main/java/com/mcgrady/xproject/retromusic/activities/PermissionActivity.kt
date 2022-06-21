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
package com.mcgrady.xproject.retromusic.activities

import android.Manifest
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.text.parseAsHtml
import androidx.core.view.isVisible
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.activities.base.AbsMusicServiceActivity
import com.mcgrady.xproject.retromusic.databinding.ActivityPermissionBinding
import com.mcgrady.xproject.retromusic.extensions.*
import com.mcgrady.xproject.theme.util.VersionUtils

class PermissionActivity : AbsMusicServiceActivity() {
    private lateinit var binding: ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColorAuto()
        setTaskDescriptionColorAuto()
        setupTitle()

        binding.storagePermission.setButtonClick {
            requestPermissions()
        }
        if (VersionUtils.hasMarshmallow()) {
            binding.audioPermission.show()
            binding.audioPermission.setButtonClick {
                if (!hasAudioPermission()) {
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    intent.data = ("package:" + applicationContext.packageName).toUri()
                    startActivity(intent)
                }
            }
        }

        if (VersionUtils.hasS()) {
            binding.bluetoothPermission.show()
            binding.bluetoothPermission.setButtonClick {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(BLUETOOTH_CONNECT),
                    BLUETOOTH_PERMISSION_REQUEST
                )
            }
        } else {
            binding.audioPermission.setNumber("2")
        }

        binding.finish.accentBackgroundColor()
        binding.finish.setOnClickListener {
            if (hasPermissions()) {
                startActivity(
                    Intent(this, MainActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                )
                finish()
            }
        }
    }

    private fun setupTitle() {
        val color = accentColor()
        val hexColor = String.format("#%06X", 0xFFFFFF and color)
        val appName =
            getString(
                R.string.message_welcome,
                "<b>Retro <span  style='color:$hexColor';>Music</span></b>"
            )
                .parseAsHtml()
        binding.appNameText.text = appName
    }

    override fun onResume() {
        super.onResume()
        binding.finish.isEnabled = hasStoragePermission()
        if (hasStoragePermission()) {
            binding.storagePermission.checkImage.isVisible = true
            binding.storagePermission.checkImage.imageTintList =
                ColorStateList.valueOf(accentColor())
        }
        if (VersionUtils.hasMarshmallow()) {
            if (hasAudioPermission()) {
                binding.audioPermission.checkImage.isVisible = true
                binding.audioPermission.checkImage.imageTintList =
                    ColorStateList.valueOf(accentColor())
            }
        }
        if (VersionUtils.hasS()) {
            if (hasBluetoothPermission()) {
                binding.bluetoothPermission.checkImage.isVisible = true
                binding.bluetoothPermission.checkImage.imageTintList =
                    ColorStateList.valueOf(accentColor())
            }
        }
    }

    private fun hasStoragePermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun hasBluetoothPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasAudioPermission(): Boolean {
        return Settings.System.canWrite(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
