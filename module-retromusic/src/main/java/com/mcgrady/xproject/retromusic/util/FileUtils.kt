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
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.IOException

object FileUtils {
    fun copyFileToUri(context: Context, fromFile: File, toUri: Uri) {
        context.contentResolver.openOutputStream(toUri)
            ?.use { output ->
                fromFile.inputStream().use { input ->
                    input.copyTo(output)
                }
            }
    }

    /**
     * creates a new file in storage in app specific directory.
     *
     * @return the file
     * @throws IOException
     */
    fun createFile(
        context: Context,
        directoryName: String,
        fileName: String,
        body: String,
        fileType: String
    ): File {
        val root = createDirectory(context, directoryName)
        val filePath = "$root/$fileName$fileType"
        val file = File(filePath)

        // create file if not exist
        if (!file.exists()) {
            try {
                // create a new file and write text in it.
                file.createNewFile()
                file.writeText(body)
                Log.d(FileUtils::class.java.name, "File has been created and saved")
            } catch (e: IOException) {
                Log.d(FileUtils::class.java.name, e.message.toString())
            }
        }
        return file
    }

    /**
     * creates a new directory in storage in app specific directory.
     *
     * @return the file
     */
    private fun createDirectory(context: Context, directoryName: String): File {
        val file = File(
            context.getExternalFilesDir(directoryName)
                .toString()
        )
        if (!file.exists()) {
            file.mkdir()
        }
        return file
    }
}
@Suppress("Deprecation")
fun getExternalStorageDirectory(): File {
    return Environment.getExternalStorageDirectory()
}

@Suppress("Deprecation")
fun getExternalStoragePublicDirectory(type: String): File {
    return Environment.getExternalStoragePublicDirectory(type)
}
