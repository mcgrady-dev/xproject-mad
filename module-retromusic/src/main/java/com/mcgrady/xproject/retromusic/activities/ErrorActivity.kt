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

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.util.FileUtils.createFile
import com.mcgrady.xproject.retromusic.util.Share.shareFile
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ErrorActivity : AppCompatActivity() {
    private val dayFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val reportPrefix = "bug_report-"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customactivityoncrash_default_error_activity)

        val restartButton =
            findViewById<Button>(R.id.customactivityoncrash_error_activity_restart_button)

        val config = CustomActivityOnCrash.getConfigFromIntent(intent)
        if (config == null) {
            finish()
            return
        }
        restartButton.setText(R.string.customactivityoncrash_error_activity_restart_app)
        restartButton.setOnClickListener {
            CustomActivityOnCrash.restartApplication(
                this@ErrorActivity,
                config
            )
        }
        val moreInfoButton =
            findViewById<Button>(R.id.customactivityoncrash_error_activity_more_info_button)

        moreInfoButton.setOnClickListener { // We retrieve all the error data and show it
            MaterialAlertDialogBuilder(this@ErrorActivity)
                .setTitle(R.string.customactivityoncrash_error_activity_error_details_title)
                .setMessage(
                    CustomActivityOnCrash.getAllErrorDetailsFromIntent(
                        this@ErrorActivity,
                        intent
                    )
                )
                .setPositiveButton(
                    R.string.customactivityoncrash_error_activity_error_details_close,
                    null
                )
                .setNeutralButton(
                    R.string.customactivityoncrash_error_activity_error_details_share
                ) { _, _ ->

                    val bugReport = createFile(
                        context = this,
                        "Bug Report",
                        "$reportPrefix${dayFormat.format(Date())}",
                        CustomActivityOnCrash.getAllErrorDetailsFromIntent(
                            this@ErrorActivity,
                            intent
                        ),
                        ".txt"
                    )
                    shareFile(this, bugReport, "text/*")
                }
                .show()
        }
        val errorActivityDrawableId = config.errorDrawable
        val errorImageView =
            findViewById<ImageView>(R.id.customactivityoncrash_error_activity_image)
        if (errorActivityDrawableId != null) {
            errorImageView.setImageResource(
                errorActivityDrawableId
            )
        }
    }
}
