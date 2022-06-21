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

import android.app.Activity
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.android.play.core.review.ReviewManagerFactory

object AppRater {
    private const val DO_NOT_SHOW_AGAIN = "do_not_show_again" // Package Name
    private const val APP_RATING = "app_rating" // Package Name
    private const val LAUNCH_COUNT = "launch_count" // Package Name
    private const val DATE_FIRST_LAUNCH = "date_first_launch" // Package Name

    private const val DAYS_UNTIL_PROMPT = 3 // Min number of days
    private const val LAUNCHES_UNTIL_PROMPT = 5 // Min number of launches

    @JvmStatic
    fun appLaunched(context: Activity) {
        val prefs = context.getSharedPreferences(APP_RATING, 0)
        if (prefs.getBoolean(DO_NOT_SHOW_AGAIN, false)) {
            return
        }

        prefs.edit {

            // Increment launch counter
            val launchCount = prefs.getLong(LAUNCH_COUNT, 0) + 1
            putLong(LAUNCH_COUNT, launchCount)

            // Get date of first launch
            var dateFirstLaunch = prefs.getLong(DATE_FIRST_LAUNCH, 0)
            if (dateFirstLaunch == 0L) {
                dateFirstLaunch = System.currentTimeMillis()
                putLong(DATE_FIRST_LAUNCH, dateFirstLaunch)
            }

            // Wait at least n days before opening
            if (launchCount >= LAUNCHES_UNTIL_PROMPT) {
                if (System.currentTimeMillis() >= dateFirstLaunch + DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000) {
                    // showRateDialog(context, editor)
                    showPlayStoreReviewDialog(context, this)
                }
            }
        }
    }

    private fun showPlayStoreReviewDialog(context: Activity, editor: SharedPreferences.Editor) {
        val manager = ReviewManagerFactory.create(context)
        val flow = manager.requestReviewFlow()
        flow.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                val reviewInfo = request.result
                val flowManager = manager.launchReviewFlow(context, reviewInfo)
                flowManager.addOnCompleteListener {
                    if (it.isSuccessful) {
                        editor.putBoolean(DO_NOT_SHOW_AGAIN, true)
                        editor.commit()
                    }
                }
            }
        }
    }
}
