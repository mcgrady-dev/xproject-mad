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
package com.mcgrady.xproject.retromusic.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.slider.Slider
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.activities.base.AbsMusicServiceActivity
import com.mcgrady.xproject.retromusic.databinding.ActivityDriveModeBinding
import com.mcgrady.xproject.retromusic.db.toSongEntity
import com.mcgrady.xproject.retromusic.extensions.accentColor
import com.mcgrady.xproject.retromusic.extensions.drawAboveSystemBars
import com.mcgrady.xproject.retromusic.glide.BlurTransformation
import com.mcgrady.xproject.retromusic.glide.GlideApp
import com.mcgrady.xproject.retromusic.glide.RetroGlideExtension
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import com.mcgrady.xproject.retromusic.helper.MusicProgressViewUpdateHelper
import com.mcgrady.xproject.retromusic.helper.MusicProgressViewUpdateHelper.Callback
import com.mcgrady.xproject.retromusic.helper.PlayPauseButtonOnClickHandler
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.repository.RealRepository
import com.mcgrady.xproject.retromusic.service.MusicService
import com.mcgrady.xproject.retromusic.util.MusicUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

/**
 * Created by hemanths on 2020-02-02.
 */

class DriveModeActivity : AbsMusicServiceActivity(), Callback {

    private lateinit var binding: ActivityDriveModeBinding
    private var lastPlaybackControlsColor: Int = Color.GRAY
    private var lastDisabledPlaybackControlsColor: Int = Color.GRAY
    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper
    private val repository: RealRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriveModeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpMusicControllers()

        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
        lastPlaybackControlsColor = accentColor()
        binding.close.setOnClickListener {
            onBackPressed()
        }
        binding.repeatButton.drawAboveSystemBars()
    }

    private fun setUpMusicControllers() {
        setUpPlayPauseFab()
        setUpPrevNext()
        setUpRepeatButton()
        setUpShuffleButton()
        setUpProgressSlider()
        setupFavouriteToggle()
    }

    private fun setupFavouriteToggle() {
        binding.songFavourite.setOnClickListener {
            toggleFavorite(MusicPlayerRemote.currentSong)
        }
    }

    private fun toggleFavorite(song: Song) {
        lifecycleScope.launch(Dispatchers.IO) {
            val playlist = repository.favoritePlaylist()
            val songEntity = song.toSongEntity(playlist.playListId)
            val isFavorite = repository.isSongFavorite(song.id)
            if (isFavorite) {
                repository.removeSongFromPlaylist(songEntity)
            } else {
                repository.insertSongs(listOf(song.toSongEntity(playlist.playListId)))
            }
            sendBroadcast(Intent(MusicService.FAVORITE_STATE_CHANGED))
        }
    }

    private fun updateFavorite() {
        lifecycleScope.launch(Dispatchers.IO) {
            val isFavorite: Boolean =
                repository.isSongFavorite(MusicPlayerRemote.currentSong.id)
            withContext(Dispatchers.Main) {
                binding.songFavourite.setImageResource(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
            }
        }
    }

    private fun setUpProgressSlider() {
        binding.progressSlider.addOnChangeListener { _: Slider, progress: Float, fromUser: Boolean ->
            if (fromUser) {
                MusicPlayerRemote.seekTo(progress.toInt())
                onUpdateProgressViews(
                    MusicPlayerRemote.songProgressMillis,
                    MusicPlayerRemote.songDurationMillis
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        progressViewUpdateHelper.stop()
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper.start()
    }

    private fun setUpPrevNext() {
        binding.nextButton.setOnClickListener { MusicPlayerRemote.playNextSong() }
        binding.previousButton.setOnClickListener { MusicPlayerRemote.back() }
    }

    private fun setUpShuffleButton() {
        binding.shuffleButton.setOnClickListener { MusicPlayerRemote.toggleShuffleMode() }
    }

    private fun setUpRepeatButton() {
        binding.repeatButton.setOnClickListener { MusicPlayerRemote.cycleRepeatMode() }
    }

    private fun setUpPlayPauseFab() {
        binding.playPauseButton.setOnClickListener(PlayPauseButtonOnClickHandler())
    }

    override fun onRepeatModeChanged() {
        super.onRepeatModeChanged()
        updateRepeatState()
    }

    override fun onShuffleModeChanged() {
        super.onShuffleModeChanged()
        updateShuffleState()
    }

    override fun onPlayStateChanged() {
        super.onPlayStateChanged()
        updatePlayPauseDrawableState()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updatePlayPauseDrawableState()
        updateSong()
        updateRepeatState()
        updateShuffleState()
        updateFavorite()
    }

    private fun updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying) {
            binding.playPauseButton.setImageResource(R.drawable.ic_pause)
        } else {
            binding.playPauseButton.setImageResource(R.drawable.ic_play_arrow)
        }
    }

    fun updateShuffleState() {
        when (MusicPlayerRemote.shuffleMode) {
            MusicService.SHUFFLE_MODE_SHUFFLE -> binding.shuffleButton.setColorFilter(
                lastPlaybackControlsColor,
                PorterDuff.Mode.SRC_IN
            )
            else -> binding.shuffleButton.setColorFilter(
                lastDisabledPlaybackControlsColor,
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun updateRepeatState() {
        when (MusicPlayerRemote.repeatMode) {
            MusicService.REPEAT_MODE_NONE -> {
                binding.repeatButton.setImageResource(R.drawable.ic_repeat)
                binding.repeatButton.setColorFilter(
                    lastDisabledPlaybackControlsColor,
                    PorterDuff.Mode.SRC_IN
                )
            }
            MusicService.REPEAT_MODE_ALL -> {
                binding.repeatButton.setImageResource(R.drawable.ic_repeat)
                binding.repeatButton.setColorFilter(
                    lastPlaybackControlsColor,
                    PorterDuff.Mode.SRC_IN
                )
            }
            MusicService.REPEAT_MODE_THIS -> {
                binding.repeatButton.setImageResource(R.drawable.ic_repeat_one)
                binding.repeatButton.setColorFilter(
                    lastPlaybackControlsColor,
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
        updateFavorite()
    }

    override fun onFavoriteStateChanged() {
        super.onFavoriteStateChanged()
        updateFavorite()
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong

        binding.songTitle.text = song.title
        binding.songText.text = song.artistName

        GlideApp.with(this)
            .load(RetroGlideExtension.getSongModel(song))
            .songCoverOptions(song)
            .transform(BlurTransformation.Builder(this).build())
            .into(binding.image)
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        binding.progressSlider.run {
            valueTo = total.toFloat()
            value = progress.toFloat().coerceIn(valueFrom, valueTo)
        }

        binding.songTotalTime.text = MusicUtil.getReadableDurationString(total.toLong())
        binding.songCurrentProgress.text = MusicUtil.getReadableDurationString(progress.toLong())
    }
}
