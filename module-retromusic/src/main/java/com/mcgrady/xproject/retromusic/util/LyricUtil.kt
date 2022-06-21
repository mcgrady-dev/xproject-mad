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

import android.util.Log
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.model.lyrics.AbsSynchronizedLyrics
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.*

/**
 * Created by hefuyi on 2016/11/8.
 */
object LyricUtil {
    private val lrcRootPath =
        getExternalStorageDirectory().toString() + "/RetroMusic/lyrics/"
    private const val TAG = "LyricUtil"
    fun writeLrcToLoc(
        title: String,
        artist: String,
        lrcContext: String
    ): File? {
        var writer: FileWriter? = null
        return try {
            val file = File(getLrcPath(title, artist))
            if (file.parentFile?.exists() != true) {
                file.parentFile?.mkdirs()
            }
            writer = FileWriter(getLrcPath(title, artist))
            writer.write(lrcContext)
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // So in Retro, Lrc file can be same folder as Music File or in RetroMusic Folder
    // In this case we pass location of the file and Contents to write to file
    fun writeLrc(song: Song, lrcContext: String) {
        var writer: FileWriter? = null
        val location: File?
        try {
            if (isLrcOriginalFileExist(song.data)) {
                location = getLocalLyricOriginalFile(song.data)
            } else if (isLrcFileExist(song.title, song.artistName)) {
                location = getLocalLyricFile(song.title, song.artistName)
            } else {
                location = File(getLrcPath(song.title, song.artistName))
                if (location.parentFile?.exists() != true) {
                    location.parentFile?.mkdirs()
                }
            }
            writer = FileWriter(location)
            writer.write(lrcContext)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun deleteLrcFile(title: String, artist: String): Boolean {
        val file = File(getLrcPath(title, artist))
        return file.delete()
    }

    private fun isLrcFileExist(title: String, artist: String): Boolean {
        val file = File(getLrcPath(title, artist))
        return file.exists()
    }

    private fun isLrcOriginalFileExist(path: String): Boolean {
        val file = File(getLrcOriginalPath(path))
        return file.exists()
    }

    private fun getLocalLyricFile(title: String, artist: String): File? {
        val file = File(getLrcPath(title, artist))
        return if (file.exists()) {
            file
        } else {
            null
        }
    }

    private fun getLocalLyricOriginalFile(path: String): File? {
        val file = File(getLrcOriginalPath(path))
        return if (file.exists()) {
            file
        } else {
            null
        }
    }

    private fun getLrcPath(title: String, artist: String): String {
        return "$lrcRootPath$title - $artist.lrc"
    }

    fun getLrcOriginalPath(filePath: String): String {
        return filePath.replace(filePath.substring(filePath.lastIndexOf(".") + 1), "lrc")
    }

    @Throws(Exception::class)
    fun getStringFromFile(title: String, artist: String): String {
        val file = File(getLrcPath(title, artist))
        val fin = FileInputStream(file)
        val ret = convertStreamToString(fin)
        fin.close()
        return ret
    }

    @Throws(Exception::class)
    private fun convertStreamToString(inputStream: InputStream): String {
        return inputStream.bufferedReader().readLines().joinToString(separator = "\n")
    }

    fun getStringFromLrc(file: File?): String {
        try {
            val reader = BufferedReader(FileReader(file))
            return reader.readLines().joinToString(separator = "\n")
        } catch (e: Exception) {
            Log.i("Error", "Error Occurred")
        }
        return ""
    }

    fun getSyncedLyricsFile(song: Song): File? {
        return when {
            isLrcOriginalFileExist(song.data) -> {
                getLocalLyricOriginalFile(song.data)
            }
            isLrcFileExist(song.title, song.artistName) -> {
                getLocalLyricFile(song.title, song.artistName)
            }
            else -> {
                null
            }
        }
    }

    fun getEmbeddedSyncedLyrics(data: String): String? {
        val embeddedLyrics = try {
            AudioFileIO.read(File(data)).tagOrCreateDefault.getFirst(FieldKey.LYRICS)
        } catch (e: Exception) {
            return null
        }
        return if (AbsSynchronizedLyrics.isSynchronized(embeddedLyrics)) {
            embeddedLyrics
        } else {
            null
        }
    }
}
