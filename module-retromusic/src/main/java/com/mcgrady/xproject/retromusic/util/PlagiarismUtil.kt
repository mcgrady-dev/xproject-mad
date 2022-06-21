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
import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.mcgrady.xproject.retromusic.BuildConfig
import com.mcgrady.xproject.retromusic.extensions.showToast

fun Activity.maybeShowAnnoyingToasts() {
    if (BuildConfig.APPLICATION_ID != "com.mcgrady.xproject.retromusic" &&
        BuildConfig.APPLICATION_ID != "com.mcgrady.xproject.retromusic.debug"
    ) {
        if (BuildConfig.DEBUG) {
            // Log these things to console, if the plagiarizer even cares to check it
            Log.d("Retro Music", "What are you doing with your life?")
            Log.d("Retro Music", "Stop copying apps and make use of your brain.")
            Log.d("Retro Music", "Stop doing this or you will end up straight to hell.")
            Log.d("Retro Music", "To the boiler room of hell. All the way down.")
        } else {
            showToast("Warning! This is a copy of Retro Music Player", Toast.LENGTH_LONG)
            showToast("Instead of using this copy by a dumb person who didn't even bother to remove this code.", Toast.LENGTH_LONG)
            showToast("Support us by downloading the original version from Play Store.", Toast.LENGTH_LONG)
            val packageName = "com.mcgrady.xproject.retromusic"
            try {
                startActivity(Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=$packageName".toUri()))
            }
        }
    }
}

fun Fragment.maybeShowAnnoyingToasts() {
    requireActivity().maybeShowAnnoyingToasts()
}
