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
import android.provider.BaseColumns
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.net.toUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.extensions.showToast
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.util.MusicUtil.getSongFileUri
import com.mcgrady.xproject.theme.util.VersionUtils

object RingtoneManager {
    fun setRingtone(context: Context, song: Song) {
        val uri = getSongFileUri(song.id)
        val resolver = context.contentResolver

        try {
            val cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.MediaColumns.TITLE),
                BaseColumns._ID + "=?",
                arrayOf(song.id.toString()), null
            )
            cursor.use { cursorSong ->
                if (cursorSong != null && cursorSong.count == 1) {
                    cursorSong.moveToFirst()
                    Settings.System.putString(resolver, Settings.System.RINGTONE, uri.toString())
                    val message = context
                        .getString(R.string.x_has_been_set_as_ringtone, cursorSong.getString(0))
                    context.showToast(message)
                }
            }
        } catch (ignored: SecurityException) {
        }
    }

    fun requiresDialog(context: Context): Boolean {
        if (VersionUtils.hasMarshmallow()) {
            if (!Settings.System.canWrite(context)) {
                return true
            }
        }
        return false
    }

    fun showDialog(context: Context) {
        return MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogTheme)
            .setTitle(R.string.dialog_title_set_ringtone)
            .setMessage(R.string.dialog_message_set_ringtone)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = ("package:" + context.applicationContext.packageName).toUri()
                context.startActivity(intent)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create().show()
    }
}
