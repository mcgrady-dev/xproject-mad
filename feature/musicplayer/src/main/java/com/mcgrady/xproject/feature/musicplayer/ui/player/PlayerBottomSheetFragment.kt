package com.mcgrady.xproject.feature.musicplayer.ui.player

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.text.toSpannable
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import coil.load
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.mcgrady.xarch.extension.viewBinding
import com.mcgrady.xproject.core.base.BaseFragment
import com.mcgrady.xproject.data.mediaplayer.MediaProgressHandler
import com.mcgrady.xproject.feature.musicplayer.R
import com.mcgrady.xproject.feature.musicplayer.databinding.FragmentPlayerBottomSheetBinding
import com.mcgrady.xproject.feature.musicplayer.service.MediaPlaybackService
import timber.log.Timber
import kotlin.math.abs

class PlayerBottomSheetFragment : BaseFragment(R.layout.fragment_player_bottom_sheet),
    View.OnClickListener, MediaProgressHandler.Callback {

    private val binding by viewBinding(FragmentPlayerBottomSheetBinding::bind)

    private lateinit var browserFuture: ListenableFuture<MediaBrowser>
    private var progressHandler: MediaProgressHandler? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_player -> {

            }
        }
    }

    override fun initialization() {
        super.initialization()
        progressHandler = MediaProgressHandler(this)
    }

    override fun onStart() {
        super.onStart()
        initializeMediaBrowser()
    }

    override fun onResume() {
        super.onResume()
        progressHandler?.start()
    }

    override fun onPause() {
        super.onPause()
        progressHandler?.stop()
    }

    override fun onStop() {
        releaseMediaBrowser()
        super.onStop()
    }

    private fun initializeMediaBrowser() {
        requireContext().let { context ->
            browserFuture = MediaBrowser.Builder(
                context,
                SessionToken(context, ComponentName(context, MediaPlaybackService::class.java))
            ).buildAsync().apply {
                addListener(browserFutureListener, MoreExecutors.directExecutor())
            }
        }
    }

    private val browserFutureListener = Runnable {
        val browserFuture = this@PlayerBottomSheetFragment.browserFuture
        setMediaControllerListener(browserFuture.get())
    }

    override fun onUpdateProgress(progress: Long, total: Long) {
    }

    override val mediaBrowser: MediaBrowser?
        get() = browserFuture.get() ?: null

    private fun setMediaControllerListener(mediaBrowser: MediaBrowser) {
        setMediaControllerUI(mediaBrowser)
        updatePlayingState(mediaBrowser.isPlaying)

        mediaBrowser.addListener(object: Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                super.onMediaMetadataChanged(mediaMetadata)
                Timber.tag(TAG).d("onMediaMetadataChanged: mediaMetadata=${mediaMetadata.title}")
                setMediaControllerUI(mediaBrowser)
                updatePlayingState(mediaBrowser.isPlaying)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                Timber.tag(TAG).d("onIsPlayingChanged: isPlaying=$isPlaying")
                updatePlayingState(isPlaying)
            }

            override fun onSkipSilenceEnabledChanged(skipSilenceEnabled: Boolean) {
                super.onSkipSilenceEnabledChanged(skipSilenceEnabled)
                Timber.tag(TAG).d("onSkipSilenceEnabledChanged: skipSilenceEnabled=$skipSilenceEnabled")
            }

            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)

                for (i in 0 until events.size()) {
                    val event = events.get(i)
                    Timber.tag(TAG).d("onEvents: event=$event")
                }
            }

            @Deprecated("Deprecated in Java")
            @SuppressLint("UnsafeOptInUsageError")
            override fun onPositionDiscontinuity(reason: Int) {
                super.onPositionDiscontinuity(reason)
                Timber.tag(TAG).d("onPositionDiscontinuity: reason=$reason")
            }
        })
    }

    private fun setMediaControllerUI(mediaBrowser: MediaBrowser) {
        val metadata = mediaBrowser.mediaMetadata
        binding.ivPlayer.setOnClickListener(this)


        //update song title
        binding.tvPlayerTitle.run {
            isSelected = true
            val artist = metadata.artist?.toSpannable()
            val builder = SpannableStringBuilder()
            metadata.title?.toSpannable()?.let { title ->
                builder.append(title)
                metadata.artist?.toSpannable()?.let { artist ->
                    builder.append(" • ").append(artist)
                }
            }

            text = builder
        }

        //update cover
        binding.ivCover.load(metadata.artworkData)
    }

    private fun updatePlayingState(isPlaying: Boolean) {
        binding.ivPlayer.setImageResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_arrow)
        progressHandler?.run { if (isPlaying) start() else stop() }
    }

    private fun releaseMediaBrowser() {
        MediaBrowser.releaseFuture(browserFuture)
    }

    class FlingPlayBackController(context: Context) : View.OnTouchListener {

        private var flingPlayBackController = GestureDetector(context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(
                    e1: MotionEvent,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if (abs(velocityX) > abs(velocityY)) {
                        if (velocityX < 0) {
                            //MusicPlayerRemote.playNextSong()
                            return true
                        } else if (velocityX > 0) {
                            //MusicPlayerRemote.playPreviousSong()
                            return true
                        }
                    }
                    return false
                }
            })

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return flingPlayBackController.onTouchEvent(event)
        }
    }

    companion object {
        const val TAG = "PlayerBottomSheet"
    }
}