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

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.mcgrady.xproject.retromusic.Constants

object UriUtil {
    @RequiresApi(Build.VERSION_CODES.Q)
    fun getUriFromPath(context: Context, path: String): Uri {
        val uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val proj = arrayOf(MediaStore.Files.FileColumns._ID)
        context.contentResolver.query(
            uri, proj, Constants.DATA + "=?", arrayOf(path), null
        )?.use { cursor ->
            if (cursor.count != 0) {
                cursor.moveToFirst()
                return ContentUris.withAppendedId(uri, cursor.getLong(0))
            }
        }
        return Uri.EMPTY
    }
}
