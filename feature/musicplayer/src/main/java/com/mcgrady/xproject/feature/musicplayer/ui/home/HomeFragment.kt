package com.mcgrady.xproject.feature.musicplayer.ui.home

import android.content.ComponentName
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.mcgrady.xarch.extension.viewBinding
import com.mcgrady.xproject.core.base.BaseFragment
import com.mcgrady.xproject.feature.musicplayer.R
import com.mcgrady.xproject.feature.musicplayer.databinding.FragmentHomeBinding
import com.mcgrady.xproject.feature.musicplayer.service.MediaPlaybackService

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnPlay.setOnClickListener {
            controller?.run {
                repeatMode = Player.REPEAT_MODE_ALL
                playWhenReady = true
                stop()
                setMediaItem(
                    MediaItem.fromUri("https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/03_-_Voyage_I_-_Waterfall.mp3")
//                    MediaItem.fromUri("https://develope-q4.lanzouk.com/0829150094117244bb/2022/12/19/04f68eea04e59eb6273ffd03a75b4577.mp3?st=ll_s9hzOJZRrSJEC9ZSXAA&e=1693297514&b=UeZZ3webA_bAF4FSTVisHQQY2DSgAbgVtADdcf1BkAzMGZAEvA25QJVYw&fi=94117244&pid=113-68-152-145&up=2&mp=0&co=0")
                )
                prepare()
            }

        }
    }

    override fun onStart() {
        super.onStart()
        initializeController()
    }

    override fun onStop() {
        super.onStop()
        releaseController()
    }

    private fun initializeController() {
        requireContext()?.let { context ->
            controllerFuture = MediaController.Builder(
                context,
                SessionToken(context, ComponentName(context, MediaPlaybackService::class.java))
            ).buildAsync()
            controllerFuture.addListener({ setController() }, MoreExecutors.directExecutor())
        }

    }

    private fun setController() {
        val controller = this.controller ?: return

    }

    private fun releaseController() {
        MediaController.releaseFuture(controllerFuture)
    }
}