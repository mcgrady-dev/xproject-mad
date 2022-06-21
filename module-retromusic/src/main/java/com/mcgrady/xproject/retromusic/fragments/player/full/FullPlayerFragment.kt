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
package com.mcgrady.xproject.retromusic.fragments.player.full

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.FragmentFullBinding
import com.mcgrady.xproject.retromusic.extensions.drawAboveSystemBars
import com.mcgrady.xproject.retromusic.extensions.hide
import com.mcgrady.xproject.retromusic.extensions.show
import com.mcgrady.xproject.retromusic.extensions.whichFragment
import com.mcgrady.xproject.retromusic.fragments.base.AbsPlayerFragment
import com.mcgrady.xproject.retromusic.fragments.base.goToArtist
import com.mcgrady.xproject.retromusic.fragments.player.PlayerAlbumCoverFragment
import com.mcgrady.xproject.retromusic.glide.GlideApp
import com.mcgrady.xproject.retromusic.glide.RetroGlideExtension
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.util.color.MediaNotificationProcessor
import com.mcgrady.xproject.theme.util.ToolbarContentTintHelper

class FullPlayerFragment : AbsPlayerFragment(R.layout.fragment_full) {
    private var _binding: FragmentFullBinding? = null
    private val binding get() = _binding!!

    override fun playerToolbar(): Toolbar {
        return binding.playerToolbar
    }

    private var lastColor: Int = 0
    override val paletteColor: Int
        get() = lastColor
    private lateinit var controlsFragment: FullPlaybackControlsFragment

    private fun setUpPlayerToolbar() {
        binding.playerToolbar.apply {
            setNavigationOnClickListener { requireActivity().onBackPressed() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFullBinding.bind(view)

        setUpSubFragments()
        setUpPlayerToolbar()
        setupArtist()
        binding.nextSong.isSelected = true
        binding.playbackControlsFragment.drawAboveSystemBars()
    }

    private fun setupArtist() {
        binding.artistImage.setOnClickListener {
            goToArtist(mainActivity)
        }
    }

    private fun setUpSubFragments() {
        controlsFragment = whichFragment(R.id.playbackControlsFragment)
        val coverFragment: PlayerAlbumCoverFragment = whichFragment(R.id.playerAlbumCoverFragment)
        coverFragment.setCallbacks(this)
        coverFragment.removeSlideEffect()
    }

    override fun onShow() {
    }

    override fun onHide() {
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun toolbarIconColor(): Int {
        return Color.WHITE
    }

    override fun onColorChanged(color: MediaNotificationProcessor) {
        lastColor = color.backgroundColor
        binding.mask.backgroundTintList = ColorStateList.valueOf(color.backgroundColor)
        controlsFragment.setColor(color)
        libraryViewModel.updateColor(color.backgroundColor)
        ToolbarContentTintHelper.colorizeToolbar(binding.playerToolbar, Color.WHITE, activity)
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
        controlsFragment.onFavoriteToggled()
    }

    override fun toggleFavorite(song: Song) {
        super.toggleFavorite(song)
        if (song.id == MusicPlayerRemote.currentSong.id) {
            updateIsFavorite()
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateArtistImage()
        updateLabel()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateArtistImage()
        updateLabel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateArtistImage() {
        libraryViewModel.artist(MusicPlayerRemote.currentSong.artistId)
            .observe(viewLifecycleOwner) { artist ->
                if (artist.id != -1L) {
                    GlideApp.with(requireActivity())
                        .load(RetroGlideExtension.getArtistModel(artist))
                        .artistImageOptions(artist)
                        .into(binding.artistImage)
                }
            }
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        if (MusicPlayerRemote.playingQueue.isNotEmpty()) updateLabel()
    }

    private fun updateLabel() {
        if ((MusicPlayerRemote.playingQueue.size - 1) == (MusicPlayerRemote.position)) {
            binding.nextSongLabel.setText(R.string.last_song)
            binding.nextSong.hide()
        } else {
            val title = MusicPlayerRemote.playingQueue[MusicPlayerRemote.position + 1].title
            binding.nextSongLabel.setText(R.string.next_song)
            binding.nextSong.apply {
                text = title
                show()
            }
        }
    }
}
