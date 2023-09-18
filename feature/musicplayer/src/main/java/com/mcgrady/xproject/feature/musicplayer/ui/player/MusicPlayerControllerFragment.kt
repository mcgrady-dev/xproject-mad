package com.mcgrady.xproject.feature.musicplayer.ui.player

import android.annotation.SuppressLint
import android.content.ComponentName
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerControlView
import androidx.viewpager2.widget.ViewPager2
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.mcgrady.xproject.feature.musicplayer.ui.base.BaseMediaPlayerFragment
import com.mcgrady.xproject.feature.musicplayer.R
import com.mcgrady.xproject.feature.musicplayer.service.MediaPlaybackService
import com.mcgrady.xproject.feature.musicplayer.ui.player.adapter.PlayerControllerPager

class MusicPlayerControllerFragment : BaseMediaPlayerFragment() {

    private lateinit var viewPagerCover: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        playerView = inflater.inflate(R.layout.fragment_music_player_controller, container, false) as PlayerControlView

        initCoverLyricsSlideView()

        return playerView
    }

    private fun initCoverLyricsSlideView() {
        viewPagerCover = playerView.findViewById<ViewPager2?>(R.id.view_pager_cover).apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = PlayerControllerPager(this@MusicPlayerControllerFragment)
            registerOnPageChangeCallback(onPageChangeCallback)
        }
    }

    override fun initializeController(): ListenableFuture<MediaController> {
        requireContext().let { context ->
            return MediaController.Builder(
                context,
                SessionToken(context, ComponentName(context, MediaPlaybackService::class.java))
            ).buildAsync().apply {
                addListener({ setController() }, MoreExecutors.directExecutor())
            }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun setController() {
        val controller = controller ?: return

        playerView.player = controller

        controller.addListener(object: Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
            }

            override fun onTracksChanged(tracks: Tracks) {
                super.onTracksChanged(tracks)
            }
        })
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }
}