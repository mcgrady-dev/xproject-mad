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
package com.mcgrady.xproject.retromusic.activities.tageditor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.shape.MaterialShapeDrawable
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.ActivitySongTagEditorBinding
import com.mcgrady.xproject.retromusic.extensions.*
import com.mcgrady.xproject.retromusic.glide.GlideApp
import com.mcgrady.xproject.retromusic.glide.palette.BitmapPaletteWrapper
import com.mcgrady.xproject.retromusic.model.ArtworkInfo
import com.mcgrady.xproject.retromusic.repository.SongRepository
import com.mcgrady.xproject.retromusic.util.ImageUtil
import com.mcgrady.xproject.retromusic.util.MusicUtil
import com.mcgrady.xproject.retromusic.util.RetroColorUtil
import com.mcgrady.xproject.retromusic.util.logD
import com.mcgrady.xproject.theme.util.MaterialValueHelper
import org.jaudiotagger.tag.FieldKey
import org.koin.android.ext.android.inject
import java.util.*

class SongTagEditorActivity : AbsTagEditorActivity<ActivitySongTagEditorBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivitySongTagEditorBinding =
        ActivitySongTagEditorBinding::inflate

    private val songRepository by inject<SongRepository>()

    private var albumArtBitmap: Bitmap? = null
    private var deleteAlbumArt: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpViews()
        setSupportActionBar(binding.toolbar)
        binding.appBarLayout?.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpViews() {
        fillViewsWithFileTags()
        binding.songTextContainer.setTint(false)
        binding.composerContainer.setTint(false)
        binding.albumTextContainer.setTint(false)
        binding.artistContainer.setTint(false)
        binding.albumArtistContainer.setTint(false)
        binding.yearContainer.setTint(false)
        binding.genreContainer.setTint(false)
        binding.trackNumberContainer.setTint(false)
        binding.discNumberContainer.setTint(false)
        binding.lyricsContainer.setTint(false)

        binding.songText.appHandleColor().doAfterTextChanged { dataChanged() }
        binding.albumText.appHandleColor().doAfterTextChanged { dataChanged() }
        binding.albumArtistText.appHandleColor().doAfterTextChanged { dataChanged() }
        binding.artistText.appHandleColor().doAfterTextChanged { dataChanged() }
        binding.genreText.appHandleColor().doAfterTextChanged { dataChanged() }
        binding.yearText.appHandleColor().doAfterTextChanged { dataChanged() }
        binding.trackNumberText.appHandleColor().doAfterTextChanged { dataChanged() }
        binding.discNumberText.appHandleColor().doAfterTextChanged { dataChanged() }
        binding.lyricsText.appHandleColor().doAfterTextChanged { dataChanged() }
        binding.songComposerText.appHandleColor().doAfterTextChanged { dataChanged() }
    }

    private fun fillViewsWithFileTags() {
        binding.songText.setText(songTitle)
        binding.albumArtistText.setText(albumArtist)
        binding.albumText.setText(albumTitle)
        binding.artistText.setText(artistName)
        binding.genreText.setText(genreName)
        binding.yearText.setText(songYear)
        binding.trackNumberText.setText(trackNumber)
        binding.discNumberText.setText(discNumber)
        binding.lyricsText.setText(lyrics)
        binding.songComposerText.setText(composer)
        logD(songTitle + songYear)
    }

    override fun loadCurrentImage() {
        val bitmap = albumArt
        setImageBitmap(
            bitmap,
            RetroColorUtil.getColor(
                RetroColorUtil.generatePalette(bitmap),
                defaultFooterColor()
            )
        )
        deleteAlbumArt = false
    }

    override fun searchImageOnWeb() {
        searchWebFor(binding.songText.text.toString(), binding.artistText.text.toString())
    }

    override fun deleteImage() {
        setImageBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.default_audio_art),
            defaultFooterColor()
        )
        deleteAlbumArt = true
        dataChanged()
    }

    override fun setColors(color: Int) {
        super.setColors(color)
        saveFab.backgroundTintList = ColorStateList.valueOf(color)
        ColorStateList.valueOf(
            MaterialValueHelper.getPrimaryTextColor(
                this,
                color.isColorLight
            )
        ).also {
            saveFab.iconTint = it
            saveFab.setTextColor(it)
        }
    }

    override fun save() {
        val fieldKeyValueMap = EnumMap<FieldKey, String>(FieldKey::class.java)
        fieldKeyValueMap[FieldKey.TITLE] = binding.songText.text.toString()
        fieldKeyValueMap[FieldKey.ALBUM] = binding.albumText.text.toString()
        fieldKeyValueMap[FieldKey.ARTIST] = binding.artistText.text.toString()
        fieldKeyValueMap[FieldKey.GENRE] = binding.genreText.text.toString()
        fieldKeyValueMap[FieldKey.YEAR] = binding.yearText.text.toString()
        fieldKeyValueMap[FieldKey.TRACK] = binding.trackNumberText.text.toString()
        fieldKeyValueMap[FieldKey.DISC_NO] = binding.discNumberText.text.toString()
        fieldKeyValueMap[FieldKey.LYRICS] = binding.lyricsText.text.toString()
        fieldKeyValueMap[FieldKey.ALBUM_ARTIST] = binding.albumArtistText.text.toString()
        fieldKeyValueMap[FieldKey.COMPOSER] = binding.songComposerText.text.toString()
        writeValuesToFiles(
            fieldKeyValueMap,
            when {
                deleteAlbumArt -> ArtworkInfo(id, null)
                albumArtBitmap == null -> null
                else -> ArtworkInfo(id, albumArtBitmap!!)
            }
        )
    }

    override fun getSongPaths(): List<String> = listOf(songRepository.song(id).data)

    override fun getSongUris(): List<Uri> = listOf(MusicUtil.getSongFileUri(id))

    override fun loadImageFromFile(selectedFile: Uri?) {
        GlideApp.with(this@SongTagEditorActivity).asBitmapPalette().load(selectedFile)
            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            .into(object : ImageViewTarget<BitmapPaletteWrapper>(binding.editorImage) {
                override fun onResourceReady(
                    resource: BitmapPaletteWrapper,
                    transition: Transition<in BitmapPaletteWrapper>?
                ) {
                    RetroColorUtil.getColor(resource.palette, Color.TRANSPARENT)
                    albumArtBitmap = resource.bitmap?.let { ImageUtil.resizeBitmap(it, 2048) }
                    setImageBitmap(
                        albumArtBitmap,
                        RetroColorUtil.getColor(
                            resource.palette,
                            defaultFooterColor()
                        )
                    )
                    deleteAlbumArt = false
                    dataChanged()
                    setResult(Activity.RESULT_OK)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    showToast(R.string.error_load_failed, Toast.LENGTH_LONG)
                }

                override fun setResource(resource: BitmapPaletteWrapper?) {}
            })
    }

    companion object {
        val TAG: String = SongTagEditorActivity::class.java.simpleName
    }

    override val editorImage: ImageView
        get() = binding.editorImage
}
