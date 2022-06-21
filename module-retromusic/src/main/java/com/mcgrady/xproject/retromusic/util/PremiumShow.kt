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
import android.content.Intent
import com.mcgrady.xproject.retromusic.App
import com.mcgrady.xproject.retromusic.activities.PurchaseActivity

object PremiumShow {
    private const val PREF_NAME = "premium_show"
    private const val LAUNCH_COUNT = "launch_count"
    private const val DATE_FIRST_LAUNCH = "date_first_launch"

    @JvmStatic
    fun launch(context: Context) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if (App.isProVersion()) {
            return
        }
        val prefEditor = pref.edit()
        val launchCount = pref.getLong(LAUNCH_COUNT, 0) + 1
        prefEditor.putLong(LAUNCH_COUNT, launchCount)

        var dateLaunched = pref.getLong(DATE_FIRST_LAUNCH, 0)
        if (dateLaunched == 0L) {
            dateLaunched = System.currentTimeMillis()
            prefEditor.putLong(DATE_FIRST_LAUNCH, dateLaunched)
        }
        if (System.currentTimeMillis() >= dateLaunched + 2 * 24 * 60 * 60 * 1000) {
            context.startActivity(Intent(context, PurchaseActivity::class.java), null)
        }
        prefEditor.apply()
    }
}
