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
package com.mcgrady.xproject.retromusic.fragments.player.gradient

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.slider.Slider
import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.adapter.song.PlayingQueueAdapter
import com.mcgrady.xproject.retromusic.databinding.FragmentGradientPlayerBinding
import com.mcgrady.xproject.retromusic.extensions.*
import com.mcgrady.xproject.retromusic.fragments.MusicSeekSkipTouchListener
import com.mcgrady.xproject.retromusic.fragments.base.AbsPlayerFragment
import com.mcgrady.xproject.retromusic.fragments.base.goToAlbum
import com.mcgrady.xproject.retromusic.fragments.base.goToArtist
import com.mcgrady.xproject.retromusic.fragments.other.VolumeFragment
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import com.mcgrady.xproject.retromusic.helper.MusicProgressViewUpdateHelper
import com.mcgrady.xproject.retromusic.helper.PlayPauseButtonOnClickHandler
import com.mcgrady.xproject.retromusic.service.MusicService
import com.mcgrady.xproject.retromusic.util.MusicUtil
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.retromusic.util.color.MediaNotificationProcessor
import com.mcgrady.xproject.theme.util.ColorUtil
import com.mcgrady.xproject.theme.util.VersionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GradientPlayerFragment :
    AbsPlayerFragment(R.layout.fragment_gradient_player),
    MusicProgressViewUpdateHelper.Callback,
    View.OnLayoutChangeListener,
    PopupMenu.OnMenuItemClickListener {
    private var lastColor: Int = 0
    private var lastPlaybackControlsColor: Int = 0
    private var lastDisabledPlaybackControlsColor: Int = 0
    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper
    private var volumeFragment: VolumeFragment? = null
    private lateinit var wrappedAdapter: RecyclerView.Adapter<*>
    private var recyclerViewDragDropManager: RecyclerViewDragDropManager? = null
    private var recyclerViewSwipeManager: RecyclerViewSwipeManager? = null
    private var recyclerViewTouchActionGuardManager: RecyclerViewTouchActionGuardManager? = null
    private var playingQueueAdapter: PlayingQueueAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var navBarHeight = 0

    private var _binding: FragmentGradientPlayerBinding? = null
    private val binding get() = _binding!!

    private var progressAnimator: ObjectAnimator? = null
    var isSeeking = false

    private val bottomSheetCallbackList = object : BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            mainActivity.getBottomSheetBehavior().isDraggable = false
            binding.playerQueueSheet.updatePadding(
                top = (slideOffset * binding.statusBarLayout.statusBar.height).toInt()
            )
            binding.container.updatePadding(
                bottom = ((1 - slideOffset) * navBarHeight).toInt()
            )
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                STATE_EXPANDED,
                STATE_DRAGGING,
                -> {
                    mainActivity.getBottomSheetBehavior().isDraggable = false
                }
                STATE_COLLAPSED -> {
                    resetToCurrentPosition()
                    mainActivity.getBottomSheetBehavior().isDraggable = true
                }
                else -> {
                    mainActivity.getBottomSheetBehavior().isDraggable = true
                }
            }
        }
    }

    private fun setupFavourite() {
        binding.playbackControlsFragment.songFavourite.setOnClickListener {
            toggleFavorite(MusicPlayerRemote.currentSong)
        }
    }

    private fun setupMenu() {
        binding.playbackControlsFragment.playerMenu.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.inflate(R.menu.menu_player)
            popupMenu.menu.findItem(R.id.action_toggle_favorite).isVisible = false
            popupMenu.menu.findItem(R.id.action_toggle_lyrics).isChecked = PreferenceUtil.showLyrics
            popupMenu.show()
        }
    }

    private fun setupPanel() {
        if (!binding.colorBackground.isLaidOut || binding.colorBackground.isLayoutRequested) {
            binding.colorBackground.addOnLayoutChangeListener(this)
            return
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGradientPlayerBinding.bind(view)
        hideVolumeIfAvailable()
        setUpMusicControllers()
        setupPanel()
        setupRecyclerView()
        setupSheet()
        setupMenu()
        setupFavourite()
        binding.playbackControlsFragment.title.setOnClickListener {
            goToAlbum(requireActivity())
        }
        binding.playbackControlsFragment.text.setOnClickListener {
            goToArtist(requireActivity())
        }
        ViewCompat.setOnApplyWindowInsetsListener(
            (binding.container)
        ) { v: View, insets: WindowInsetsCompat ->
            navBarHeight = insets.getBottomInsets()
            v.updatePadding(bottom = navBarHeight)
            insets
        }
        binding.playbackControlsFragment.root.drawAboveSystemBars()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSheet() {
        getQueuePanel().addBottomSheetCallback(bottomSheetCallbackList)
        binding.playerQueueSheet.setOnTouchListener { _, _ ->
            mainActivity.getBottomSheetBehavior().isDraggable = false
            getQueuePanel().isDraggable = true
            return@setOnTouchListener false
        }
    }

    private fun getQueuePanel(): BottomSheetBehavior<ConstraintLayout> {
        return from(binding.playerQueueSheet)
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper.start()
    }

    override fun onPause() {
        super.onPause()
        recyclerViewDragDropManager?.cancelDrag()
        progressViewUpdateHelper.stop()
    }

    override fun playerToolbar(): Toolbar? {
        return null
    }

    override fun onShow() {
    }

    override fun onHide() {
    }

    override fun onBackPressed(): Boolean {
        var wasExpanded = false
        if (getQueuePanel().state == STATE_EXPANDED) {
            wasExpanded = getQueuePanel().state == STATE_EXPANDED
            getQueuePanel().state = STATE_COLLAPSED
            return wasExpanded
        }
        return wasExpanded
    }

    override fun toolbarIconColor(): Int {
        return Color.WHITE
    }

    override val paletteColor: Int
        get() = lastColor

    override fun onColorChanged(color: MediaNotificationProcessor) {
        lastColor = color.backgroundColor
        libraryViewModel.updateColor(color.backgroundColor)
        binding.mask.backgroundTintList = ColorStateList.valueOf(color.backgroundColor)
        binding.colorBackground.setBackgroundColor(color.backgroundColor)
        binding.playerQueueSheet.setBackgroundColor(ColorUtil.darkenColor(color.backgroundColor))

        lastPlaybackControlsColor = color.primaryTextColor
        lastDisabledPlaybackControlsColor = ColorUtil.withAlpha(color.primaryTextColor, 0.3f)

        binding.playbackControlsFragment.title.setTextColor(lastPlaybackControlsColor)
        binding.playbackControlsFragment.text.setTextColor(lastDisabledPlaybackControlsColor)
        binding.playbackControlsFragment.playPauseButton.setColorFilter(
            lastPlaybackControlsColor,
            PorterDuff.Mode.SRC_IN
        )
        binding.playbackControlsFragment.nextButton.setColorFilter(
            lastPlaybackControlsColor,
            PorterDuff.Mode.SRC_IN
        )
        binding.playbackControlsFragment.previousButton.setColorFilter(
            lastPlaybackControlsColor,
            PorterDuff.Mode.SRC_IN
        )
        binding.playbackControlsFragment.songFavourite.setColorFilter(
            lastPlaybackControlsColor,
            PorterDuff.Mode.SRC_IN
        )
        binding.queueIcon.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
        binding.playbackControlsFragment.playerMenu.setColorFilter(
            lastPlaybackControlsColor,
            PorterDuff.Mode.SRC_IN
        )
        binding.playbackControlsFragment.songCurrentProgress.setTextColor(
            lastDisabledPlaybackControlsColor
        )
        binding.playbackControlsFragment.songTotalTime.setTextColor(
            lastDisabledPlaybackControlsColor
        )
        binding.nextSong.setTextColor(lastPlaybackControlsColor)
        binding.playbackControlsFragment.songInfo.setTextColor(lastDisabledPlaybackControlsColor)

        volumeFragment?.setTintableColor(lastPlaybackControlsColor.ripAlpha())

        binding.playbackControlsFragment.progressSlider.applyColor(lastPlaybackControlsColor.ripAlpha())

        updateRepeatState()
        updateShuffleState()
        updatePrevNextColor()
    }

    override fun onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.currentSong)
    }

    private fun updateIsFavoriteIcon(animate: Boolean = false) {
        lifecycleScope.launch(Dispatchers.IO) {
            val isFavorite: Boolean =
                libraryViewModel.isSongFavorite(MusicPlayerRemote.currentSong.id)
            withContext(Dispatchers.Main) {
                val icon = if (animate && VersionUtils.hasMarshmallow()) {
                    if (isFavorite) R.drawable.avd_favorite else R.drawable.avd_unfavorite
                } else {
                    if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                }
                binding.playbackControlsFragment.songFavourite.apply {
                    setImageResource(icon)
                    drawable.also {
                        if (it is AnimatedVectorDrawable) {
                            it.start()
                        }
                    }
                }
            }
        }
    }

    private fun hideVolumeIfAvailable() {
        if (PreferenceUtil.isVolumeVisibilityMode) {
            childFragmentManager.commit {
                replace(R.id.volumeFragmentContainer, VolumeFragment.newInstance())
            }
            childFragmentManager.executePendingTransactions()
            volumeFragment =
                whichFragment(R.id.volumeFragmentContainer) as VolumeFragment?
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateSong()
        updatePlayPauseDrawableState()
        updateQueue()
        updateIsFavoriteIcon()
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

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSong()
        updateQueuePosition()
        updateIsFavoriteIcon()
    }

    override fun onFavoriteStateChanged() {
        updateIsFavoriteIcon(animate = true)
    }

    override fun onQueueChanged() {
        super.onQueueChanged()
        updateLabel()
        playingQueueAdapter?.swapDataSet(MusicPlayerRemote.playingQueue)
    }

    private fun updateSong() {
        val song = MusicPlayerRemote.currentSong
        binding.playbackControlsFragment.title.text = song.title
        binding.playbackControlsFragment.text.text = song.artistName
        updateLabel()
        if (PreferenceUtil.isSongInfo) {
            binding.playbackControlsFragment.songInfo.text = getSongInfo(song)
            binding.playbackControlsFragment.songInfo.show()
        } else {
            binding.playbackControlsFragment.songInfo.hide()
        }
    }

    private fun setUpMusicControllers() {
        setUpPlayPauseFab()
        setUpPrevNext()
        setUpRepeatButton()
        setUpShuffleButton()
        setUpProgressSlider()
        binding.playbackControlsFragment.title.isSelected = true
        binding.playbackControlsFragment.text.isSelected = true
    }

    private fun updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying) {
            binding.playbackControlsFragment.playPauseButton.setImageResource(R.drawable.ic_pause_white_64dp)
        } else {
            binding.playbackControlsFragment.playPauseButton.setImageResource(R.drawable.ic_play_arrow_white_64dp)
        }
    }

    private fun setUpPlayPauseFab() {
        binding.playbackControlsFragment.playPauseButton.setOnClickListener(
            PlayPauseButtonOnClickHandler()
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpPrevNext() {
        updatePrevNextColor()
        binding.playbackControlsFragment.nextButton.setOnTouchListener(
            MusicSeekSkipTouchListener(
                requireActivity(),
                true
            )
        )
        binding.playbackControlsFragment.previousButton.setOnTouchListener(
            MusicSeekSkipTouchListener(requireActivity(), false)
        )
    }

    private fun updatePrevNextColor() {
        binding.playbackControlsFragment.nextButton.setColorFilter(
            lastPlaybackControlsColor,
            PorterDuff.Mode.SRC_IN
        )
        binding.playbackControlsFragment.previousButton.setColorFilter(
            lastPlaybackControlsColor,
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun setUpShuffleButton() {
        binding.shuffleButton.setOnClickListener { MusicPlayerRemote.toggleShuffleMode() }
    }

    fun updateShuffleState() {
        when (MusicPlayerRemote.shuffleMode) {
            MusicService.SHUFFLE_MODE_SHUFFLE ->
                binding.shuffleButton.setColorFilter(
                    lastPlaybackControlsColor,
                    PorterDuff.Mode.SRC_IN
                )
            else -> binding.shuffleButton.setColorFilter(
                lastDisabledPlaybackControlsColor,
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun setUpRepeatButton() {
        binding.repeatButton.setOnClickListener { MusicPlayerRemote.cycleRepeatMode() }
    }

    fun updateRepeatState() {
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

    private fun updateLabel() {
        (MusicPlayerRemote.playingQueue.size - 1).apply {
            if (this == (MusicPlayerRemote.position)) {
                binding.nextSong.text = context?.resources?.getString(R.string.last_song)
            } else {
                val title = MusicPlayerRemote.playingQueue[MusicPlayerRemote.position + 1].title
                binding.nextSong.text = title
            }
        }
    }

    override fun onLayoutChange(
        v: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int,
    ) {
        val panel = getQueuePanel()
        if (panel.state == STATE_COLLAPSED) {
            panel.peekHeight = binding.container.height
        } else if (panel.state == STATE_EXPANDED) {
            panel.peekHeight = binding.container.height + navBarHeight
        }
    }

    private fun setupRecyclerView() {
        playingQueueAdapter = PlayingQueueAdapter(
            requireActivity() as AppCompatActivity,
            MusicPlayerRemote.playingQueue.toMutableList(),
            MusicPlayerRemote.position,
            R.layout.item_queue
        )
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerViewTouchActionGuardManager = RecyclerViewTouchActionGuardManager()
        recyclerViewDragDropManager = RecyclerViewDragDropManager()
        recyclerViewSwipeManager = RecyclerViewSwipeManager()

        val animator = DraggableItemAnimator()
        animator.supportsChangeAnimations = false
        wrappedAdapter =
            recyclerViewDragDropManager?.createWrappedAdapter(playingQueueAdapter!!) as RecyclerView.Adapter<*>
        wrappedAdapter =
            recyclerViewSwipeManager?.createWrappedAdapter(wrappedAdapter) as RecyclerView.Adapter<*>
        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = wrappedAdapter
            itemAnimator = animator
            recyclerViewTouchActionGuardManager?.attachRecyclerView(this)
            recyclerViewDragDropManager?.attachRecyclerView(this)
            recyclerViewSwipeManager?.attachRecyclerView(this)
        }

        linearLayoutManager.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getQueuePanel().removeBottomSheetCallback(bottomSheetCallbackList)
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager?.release()
            recyclerViewDragDropManager = null
        }

        if (recyclerViewSwipeManager != null) {
            recyclerViewSwipeManager?.release()
            recyclerViewSwipeManager = null
        }

        WrapperAdapterUtils.releaseAll(wrappedAdapter)
        _binding = null
    }

    private fun updateQueuePosition() {
        playingQueueAdapter?.setCurrent(MusicPlayerRemote.position)
        resetToCurrentPosition()
    }

    private fun updateQueue() {
        playingQueueAdapter?.swapDataSet(MusicPlayerRemote.playingQueue, MusicPlayerRemote.position)
        resetToCurrentPosition()
    }

    private fun resetToCurrentPosition() {
        binding.recyclerView.stopScroll()
        linearLayoutManager.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)
    }

    private fun setUpProgressSlider() {
        val progressSlider = binding.playbackControlsFragment.progressSlider
        progressSlider.addOnChangeListener(
            Slider.OnChangeListener { _, value, fromUser ->
                if (fromUser) {
                    onUpdateProgressViews(
                        value.toInt(),
                        MusicPlayerRemote.songDurationMillis
                    )
                }
            }
        )
        progressSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                isSeeking = true
                progressViewUpdateHelper.stop()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                isSeeking = false
                MusicPlayerRemote.seekTo(slider.value.toInt())
                progressViewUpdateHelper.start()
            }
        })
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        val progressSlider = binding.playbackControlsFragment.progressSlider
        progressSlider.valueTo = total.toFloat()

        progressSlider.value =
            progress.toFloat().coerceIn(progressSlider.valueFrom, progressSlider.valueTo)

        binding.playbackControlsFragment.songTotalTime.text =
            MusicUtil.getReadableDurationString(total.toLong())
        binding.playbackControlsFragment.songCurrentProgress.text =
            MusicUtil.getReadableDurationString(progress.toLong())
    }
}
