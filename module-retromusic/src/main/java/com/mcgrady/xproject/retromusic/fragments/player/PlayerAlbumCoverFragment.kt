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
package com.mcgrady.xproject.retromusic.fragments.player

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.mcgrady.xproject.retromusic.LYRICS_TYPE
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.SHOW_LYRICS
import com.mcgrady.xproject.retromusic.adapter.album.AlbumCoverPagerAdapter
import com.mcgrady.xproject.retromusic.adapter.album.AlbumCoverPagerAdapter.AlbumCoverFragment
import com.mcgrady.xproject.retromusic.databinding.FragmentPlayerAlbumCoverBinding
import com.mcgrady.xproject.retromusic.extensions.isColorLight
import com.mcgrady.xproject.retromusic.extensions.surfaceColor
import com.mcgrady.xproject.retromusic.fragments.NowPlayingScreen.*
import com.mcgrady.xproject.retromusic.fragments.base.AbsMusicServiceFragment
import com.mcgrady.xproject.retromusic.fragments.base.goToLyrics
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import com.mcgrady.xproject.retromusic.helper.MusicProgressViewUpdateHelper
import com.mcgrady.xproject.retromusic.lyrics.CoverLrcView
import com.mcgrady.xproject.retromusic.model.lyrics.Lyrics
import com.mcgrady.xproject.retromusic.transform.CarousalPagerTransformer
import com.mcgrady.xproject.retromusic.transform.ParallaxPagerTransformer
import com.mcgrady.xproject.retromusic.util.LyricUtil
import com.mcgrady.xproject.retromusic.util.LyricsType
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.retromusic.util.color.MediaNotificationProcessor
import com.mcgrady.xproject.retromusic.util.logD
import com.mcgrady.xproject.theme.util.ColorUtil
import com.mcgrady.xproject.theme.util.MaterialValueHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerAlbumCoverFragment :
    AbsMusicServiceFragment(R.layout.fragment_player_album_cover),
    ViewPager.OnPageChangeListener,
    MusicProgressViewUpdateHelper.Callback,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var _binding: FragmentPlayerAlbumCoverBinding? = null
    private val binding get() = _binding!!
    private var callbacks: Callbacks? = null
    private var currentPosition: Int = 0
    val viewPager get() = binding.viewPager

    private val colorReceiver = object : AlbumCoverFragment.ColorReceiver {
        override fun onColorReady(color: MediaNotificationProcessor, request: Int) {
            if (currentPosition == request) {
                notifyColorChange(color)
            }
        }
    }
    private var progressViewUpdateHelper: MusicProgressViewUpdateHelper? = null

    private val lrcView: CoverLrcView get() = binding.lyricsView

    var lyrics: Lyrics? = null

    fun removeSlideEffect() {
        val transformer = ParallaxPagerTransformer(R.id.player_image)
        transformer.setSpeed(0.3f)
        lifecycleScope.launchWhenStarted {
            viewPager.setPageTransformer(false, transformer)
        }
    }

    private fun updateLyrics() {
        val song = MusicPlayerRemote.currentSong
        lifecycleScope.launch(Dispatchers.IO) {
            val lrcFile = LyricUtil.getSyncedLyricsFile(song)
            if (lrcFile != null) {
                binding.lyricsView.loadLrc(lrcFile)
            } else {
                val embeddedLyrics = LyricUtil.getEmbeddedSyncedLyrics(song.data)
                if (embeddedLyrics != null) {
                    binding.lyricsView.loadLrc(embeddedLyrics)
                } else {
                    binding.lyricsView.reset()
                    binding.lyricsView.setLabel(context?.getString(R.string.no_lyrics_found))
                }
            }
        }
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        binding.lyricsView.updateTime(progress.toLong())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPlayerAlbumCoverBinding.bind(view)
        setupViewPager()
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this, 500, 1000)
        maybeInitLyrics()
        lrcView.apply {
            setDraggable(true) { time ->
                MusicPlayerRemote.seekTo(time.toInt())
                MusicPlayerRemote.resumePlaying()
                true
            }
            setOnClickListener {
                goToLyrics(requireActivity())
            }
        }
    }

    private fun setupViewPager() {
        binding.viewPager.addOnPageChangeListener(this)
        val nps = PreferenceUtil.nowPlayingScreen

        if (nps == Full || nps == Classic || nps == Fit || nps == Gradient) {
            binding.viewPager.offscreenPageLimit = 2
        } else if (PreferenceUtil.isCarouselEffect) {
            val metrics = resources.displayMetrics
            val ratio = metrics.heightPixels.toFloat() / metrics.widthPixels.toFloat()
            binding.viewPager.clipToPadding = false
            val padding =
                if (ratio >= 1.777f) {
                    40
                } else {
                    100
                }
            binding.viewPager.setPadding(padding, 0, padding, 0)
            binding.viewPager.pageMargin = 0
            binding.viewPager.setPageTransformer(false, CarousalPagerTransformer(requireContext()))
        } else {
            binding.viewPager.offscreenPageLimit = 2
            binding.viewPager.setPageTransformer(
                true,
                PreferenceUtil.albumCoverTransform
            )
        }
    }

    override fun onResume() {
        super.onResume()
        maybeInitLyrics()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(this)
        binding.viewPager.removeOnPageChangeListener(this)
        progressViewUpdateHelper?.stop()
        _binding = null
    }

    override fun onServiceConnected() {
        logD("Service Connected")
        updatePlayingQueue()
        updateLyrics()
    }

    override fun onPlayingMetaChanged() {
        logD("Playing Meta Changed")
        if (viewPager.currentItem != MusicPlayerRemote.position) {
            viewPager.setCurrentItem(MusicPlayerRemote.position, true)
        }
        updateLyrics()
    }

    override fun onQueueChanged() {
        logD("Queue Changed")
        updatePlayingQueue()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        when (key) {
            SHOW_LYRICS -> {
                if (PreferenceUtil.showLyrics) {
                    maybeInitLyrics()
                } else {
                    showLyrics(false)
                    progressViewUpdateHelper?.stop()
                }
            }
            LYRICS_TYPE -> {
                maybeInitLyrics()
            }
        }
    }

    private fun setLRCViewColors(@ColorInt primaryColor: Int, @ColorInt secondaryColor: Int) {
        lrcView.apply {
            setCurrentColor(primaryColor)
            setTimeTextColor(primaryColor)
            setTimelineColor(primaryColor)
            setNormalColor(secondaryColor)
            setTimelineTextColor(primaryColor)
        }
    }

    private fun showLyrics(visible: Boolean) {
        binding.coverLyrics.isVisible = false
        binding.lyricsView.isVisible = false
        binding.viewPager.isVisible = true
        val lyrics: View = if (PreferenceUtil.lyricsType == LyricsType.REPLACE_COVER) {
            ObjectAnimator.ofFloat(viewPager, View.ALPHA, if (visible) 0F else 1F).start()
            lrcView
        } else {
            ObjectAnimator.ofFloat(viewPager, View.ALPHA, 1F).start()
            binding.coverLyrics
        }
        ObjectAnimator.ofFloat(lyrics, View.ALPHA, if (visible) 1F else 0F).apply {
            doOnEnd {
                lyrics.isVisible = visible
            }
            start()
        }
    }

    private fun maybeInitLyrics() {
        val nps = PreferenceUtil.nowPlayingScreen
        // Don't show lyrics container for below conditions
        if (lyricViewNpsList.contains(nps) && PreferenceUtil.showLyrics) {
            showLyrics(true)
            if (PreferenceUtil.lyricsType == LyricsType.REPLACE_COVER) {
                progressViewUpdateHelper?.start()
            }
        } else {
            showLyrics(false)
            progressViewUpdateHelper?.stop()
        }
    }

    private fun updatePlayingQueue() {
        binding.viewPager.apply {
            adapter = AlbumCoverPagerAdapter(childFragmentManager, MusicPlayerRemote.playingQueue)
            adapter?.notifyDataSetChanged()
            setCurrentItem(MusicPlayerRemote.position, true)
            onPageSelected(MusicPlayerRemote.position)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        logD("Page Selected $position")
        currentPosition = position
        if (binding.viewPager.adapter != null) {
            (binding.viewPager.adapter as AlbumCoverPagerAdapter).receiveColor(
                colorReceiver,
                position
            )
        }
        if (position != MusicPlayerRemote.position) {
            MusicPlayerRemote.playSongAt(position)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    private fun notifyColorChange(color: MediaNotificationProcessor) {
        callbacks?.onColorChanged(color)
        val primaryColor = MaterialValueHelper.getPrimaryTextColor(
            requireContext(),
            surfaceColor().isColorLight
        )
        val secondaryColor = MaterialValueHelper.getSecondaryDisabledTextColor(
            requireContext(),
            surfaceColor().isColorLight
        )

        when (PreferenceUtil.nowPlayingScreen) {
            Flat, Normal, Material -> if (PreferenceUtil.isAdaptiveColor) {
                setLRCViewColors(color.primaryTextColor, color.secondaryTextColor)
            } else {
                setLRCViewColors(primaryColor, secondaryColor)
            }
            Color, Classic -> setLRCViewColors(color.primaryTextColor, color.secondaryTextColor)
            Blur -> setLRCViewColors(Color.WHITE, ColorUtil.withAlpha(Color.WHITE, 0.5f))
            else -> setLRCViewColors(primaryColor, secondaryColor)
        }
    }

    fun setCallbacks(listener: Callbacks) {
        callbacks = listener
    }

    interface Callbacks {

        fun onColorChanged(color: MediaNotificationProcessor)

        fun onFavoriteToggled()
    }

    companion object {
        val TAG: String = PlayerAlbumCoverFragment::class.java.simpleName
    }

    private val lyricViewNpsList =
        listOf(Blur, Classic, Color, Flat, Material, MD3, Normal, Plain, Simple)
}
