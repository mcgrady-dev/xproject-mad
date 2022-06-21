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
package com.mcgrady.xproject.retromusic.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import java.io.OutputStream

fun Fragment.createNewFile(
    mimeType: String,
    fileName: String,
    write: (outputStream: OutputStream?, data: Uri?) -> Unit
) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = mimeType
    intent.putExtra(Intent.EXTRA_TITLE, fileName)
    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                write(
                    context?.contentResolver?.openOutputStream(result.data?.data!!),
                    result.data?.data
                )
            }
        }
    startForResult.launch(intent)
}

fun Context.openUrl(url: String) {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = url.toUri()
    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(i)
}

fun Fragment.openUrl(url: String) {
    requireContext().openUrl(url)
}
