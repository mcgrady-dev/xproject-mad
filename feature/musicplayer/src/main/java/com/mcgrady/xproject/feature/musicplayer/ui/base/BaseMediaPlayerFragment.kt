package com.mcgrady.xproject.feature.musicplayer.ui.base

import android.annotation.SuppressLint
import android.view.View
import androidx.media3.session.MediaController
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.PlayerView
import com.google.common.util.concurrent.ListenableFuture
import com.mcgrady.xproject.core.base.BaseFragment

abstract class BaseMediaPlayerFragment : BaseFragment() {

    protected lateinit var playerView: PlayerControlView

    protected lateinit var controllerFuture: ListenableFuture<MediaController>
    protected val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null

    abstract fun initializeController(): ListenableFuture<MediaController>
    protected fun releaseController() {
        MediaController.releaseFuture(controllerFuture)
    }

    override fun onStart() {
        super.onStart()
        controllerFuture = initializeController()
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onStop() {
        super.onStop()
        playerView.player = null
        releaseController()
    }
}