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
package com.mcgrady.xproject.retromusic.fragments.player.card

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.FragmentCardPlayerPlaybackControlsBinding
import com.mcgrady.xproject.retromusic.extensions.*
import com.mcgrady.xproject.retromusic.fragments.base.AbsPlayerControlsFragment
import com.mcgrady.xproject.retromusic.fragments.base.goToAlbum
import com.mcgrady.xproject.retromusic.fragments.base.goToArtist
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.retromusic.util.color.MediaNotificationProcessor
import com.mcgrady.xproject.theme.util.ATHUtil
import com.mcgrady.xproject.theme.util.ColorUtil
import com.mcgrady.xproject.theme.util.MaterialValueHelper
import com.mcgrady.xproject.theme.util.TintHelper

class CardPlaybackControlsFragment :
    AbsPlayerControlsFragment(R.layout.fragment_card_player_playback_controls) {

    private var _binding: FragmentCardPlayerPlaybackControlsBinding? = null
    private val binding get() = _binding!!

    override val seekBar: SeekBar
        get() = binding.progressSlider

    override val shuffleButton: ImageButton
        get() = binding.mediaButton.shuffleButton

    override val repeatButton: ImageButton
        get() = binding.mediaButton.repeatButton

    override val nextButton: ImageButton
        get() = binding.mediaButton.nextButton

    override val previousButton: ImageButton
        get() = binding.mediaButton.previousButton

    override val songTotalTime: TextView
        get() = binding.songTotalTime

    override val songCurrentProgress: TextView
        get() = binding.songCurrentProgress

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCardPlayerPlaybackControlsBinding.bind(view)
        setUpPlayPauseFab()
        binding.title.isSelected = true
        binding.text.isSelected = true
        binding.title.setOnClickListener {
            goToAlbum(requireActivity())
        }
        binding.text.setOnClickListener {
            goToArtist(requireActivity())
        }
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        binding.title.text = song.title
        binding.text.text = song.artistName

        if (PreferenceUtil.isSongInfo) {
            binding.songInfo.text = getSongInfo(MusicPlayerRemote.currentSong)
            binding.songInfo.show()
        } else {
            binding.songInfo.hide()
        }
    }

    override fun onServiceConnected() {
        updatePlayPauseDrawableState()
        updateRepeatState()
        updateShuffleState()
        updateSong()
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
    }

    override fun onPlayStateChanged() {
        updatePlayPauseDrawableState()
    }

    override fun onRepeatModeChanged() {
        updateRepeatState()
    }

    override fun onShuffleModeChanged() {
        updateShuffleState()
    }

    override fun setColor(color: MediaNotificationProcessor) {
        if (!ATHUtil.isWindowBackgroundDark(requireContext())
        ) {
            lastPlaybackControlsColor = MaterialValueHelper.getSecondaryTextColor(activity, true)
            lastDisabledPlaybackControlsColor =
                MaterialValueHelper.getSecondaryDisabledTextColor(activity, true)
        } else {
            lastPlaybackControlsColor = MaterialValueHelper.getPrimaryTextColor(activity, false)
            lastDisabledPlaybackControlsColor =
                MaterialValueHelper.getPrimaryDisabledTextColor(activity, false)
        }

        updateRepeatState()
        updateShuffleState()
        updatePrevNextColor()
        updatePlayPauseColor()
        updateProgressTextColor()

        val colorFinal = if (PreferenceUtil.isAdaptiveColor) {
            color.primaryTextColor
        } else {
            accentColor().ripAlpha()
        }
        binding.image.setColorFilter(colorFinal, PorterDuff.Mode.SRC_IN)
        TintHelper.setTintAuto(
            binding.mediaButton.playPauseButton,
            MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(colorFinal)),
            false
        )
        TintHelper.setTintAuto(binding.mediaButton.playPauseButton, colorFinal, true)

        volumeFragment?.setTintable(colorFinal)
    }

    private fun updatePlayPauseColor() {
        // playPauseButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
    }

    private fun setUpPlayPauseFab() {
        binding.mediaButton.playPauseButton.setOnClickListener {
            if (MusicPlayerRemote.isPlaying) {
                MusicPlayerRemote.pauseSong()
            } else {
                MusicPlayerRemote.resumePlaying()
            }
            it.showBounceAnimation()
        }
    }

    private fun updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying) {
            binding.mediaButton.playPauseButton.setImageResource(R.drawable.ic_pause)
        } else {
            binding.mediaButton.playPauseButton.setImageResource(R.drawable.ic_play_arrow_white_32dp)
        }
    }

    private fun updateProgressTextColor() {
        val color = MaterialValueHelper.getPrimaryTextColor(context, false)
        binding.songTotalTime.setTextColor(color)
        binding.songCurrentProgress.setTextColor(color)
    }

    public override fun show() {}

    public override fun hide() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
