package com.mcgrady.xproject.feature.musicplayer.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.os.Bundle
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.common.Tracks
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.mcgrady.xarch.extension.viewBinding
import com.mcgrady.xproject.core.base.BaseActivity
import com.mcgrady.xproject.feature.musicplayer.databinding.ActivitySimpleMediaPlayerBinding
import com.mcgrady.xproject.feature.musicplayer.service.MediaPlaybackService
import com.therouter.router.Route

@Route(path = "feature/musicplayer/simplemediaplayer")
class SimpleMediaPlayerActivity : BaseActivity() {

    private val binding by viewBinding(ActivitySimpleMediaPlayerBinding::inflate)

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnStart.setOnClickListener {
            if (savedInstanceState == null) {
                binding.playerView.player?.run {
                    stop()
                    setMediaItem(MediaItem.fromUri("https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/03_-_Voyage_I_-_Waterfall.mp3"))
                    prepare()

                    it.isEnabled = false
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initializeController()
    }

    override fun onResume() {
        super.onResume()
        binding.playerView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.playerView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.playerView.player = null
        releaseController()
    }

    private fun initializeController() {
        controllerFuture = MediaController.Builder(
            this,
            SessionToken(this, ComponentName(this, MediaPlaybackService::class.java))
        ).buildAsync()
        controllerFuture.addListener({ setController() }, MoreExecutors.directExecutor())

    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun setController() {
        val controller = this.controller ?: return

        binding.playerView.player = controller.apply {
            addListener(playerListener)
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
        }

//        updateCurrentPlayListUI()
//        updateMediaMetadataUI(controller.mediaMetadata)
        binding.playerView.setShowSubtitleButton(controller.currentTracks.isTypeSupported(C.TRACK_TYPE_TEXT))

        controller.addListener(object: Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
//                updateMediaMetadataUI(mediaItem?.mediaMetadata ?: MediaMetadata.EMPTY)
            }

            override fun onTracksChanged(tracks: Tracks) {
                binding.playerView.setShowSubtitleButton(tracks.isTypeSupported(C.TRACK_TYPE_TEXT))
            }
        })
    }

    private val playerListener = object: Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
        }
    }

    private fun releaseController() {
        MediaController.releaseFuture(controllerFuture)
    }
}