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
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.edit
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mcgrady.xproject.retromusic.App
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.extensions.showToast
import com.mcgrady.xproject.retromusic.glide.GlideApp
import com.mcgrady.xproject.retromusic.model.Artist
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.*

class CustomArtistImageUtil private constructor(context: Context) {

    private val mPreferences: SharedPreferences = context.applicationContext.getSharedPreferences(
        CUSTOM_ARTIST_IMAGE_PREFS,
        Context.MODE_PRIVATE
    )

    suspend fun setCustomArtistImage(artist: Artist, uri: Uri) {
        val context = App.getContext()
        withContext(IO) {
            runCatching {
                GlideApp.with(context)
                    .asBitmap()
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .submit()
                    .get()
            }
                .onSuccess {
                    saveImage(context, artist, it)
                }
                .onFailure {
                    context.showToast(R.string.error_load_failed)
                }
        }
    }

    private fun saveImage(context: Context, artist: Artist, bitmap: Bitmap) {
        val dir = File(context.filesDir, FOLDER_NAME)
        if (!dir.exists()) {
            if (!dir.mkdirs()) { // create the folder
                return
            }
        }
        val file = File(dir, getFileName(artist))

        var successful = false
        try {
            file.outputStream().buffered().use { bos ->
                successful = ImageUtil.resizeBitmap(bitmap, 2048)
                    .compress(Bitmap.CompressFormat.JPEG, 100, bos)
            }
        } catch (e: IOException) {
            context.showToast(e.toString(), Toast.LENGTH_LONG)
        }

        if (successful) {
            mPreferences.edit { putBoolean(getFileName(artist), true) }
            ArtistSignatureUtil.getInstance(context)
                .updateArtistSignature(artist.name)
            context.contentResolver.notifyChange(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                null
            ) // trigger media store changed to force artist image reload
        }
    }

    suspend fun resetCustomArtistImage(artist: Artist) {
        withContext(IO) {
            mPreferences.edit { putBoolean(getFileName(artist), false) }
            ArtistSignatureUtil.getInstance(App.getContext()).updateArtistSignature(artist.name)
            App.getContext().contentResolver.notifyChange(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                null
            ) // trigger media store changed to force artist image reload

            val file = getFile(artist)
            if (file.exists()) {
                file.delete()
            }
        }
    }

    // shared prefs saves us many IO operations
    fun hasCustomArtistImage(artist: Artist): Boolean {
        return mPreferences.getBoolean(getFileName(artist), false)
    }

    companion object {
        private const val CUSTOM_ARTIST_IMAGE_PREFS = "custom_artist_image"
        private const val FOLDER_NAME = "/custom_artist_images/"

        private var sInstance: CustomArtistImageUtil? = null

        fun getInstance(context: Context): CustomArtistImageUtil {
            if (sInstance == null) {
                sInstance = CustomArtistImageUtil(context.applicationContext)
            }
            return sInstance!!
        }

        fun getFileName(artist: Artist): String {
            var artistName = artist.name
            // replace everything that is not a letter or a number with _
            artistName = artistName.replace("[^a-zA-Z0-9]".toRegex(), "_")
            return String.format(Locale.US, "#%d#%s.jpeg", artist.id, artistName)
        }

        @JvmStatic
        fun getFile(artist: Artist): File {
            val dir = File(App.getContext().filesDir, FOLDER_NAME)
            return File(dir, getFileName(artist))
        }
    }
}
