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
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/**
 * Created by hemanths on 2020-02-02.
 */

object Share {
    fun shareStoryToSocial(context: Context, uri: Uri) {
        val feedIntent = Intent(Intent.ACTION_SEND)
        feedIntent.type = "image/*"
        feedIntent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(feedIntent, null)
    }

    fun shareFile(context: Context, file: File, mimeType: String) {
        Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName,
                    file
                )
            )
            context.startActivity(Intent.createChooser(this, null))
        }
    }
}
