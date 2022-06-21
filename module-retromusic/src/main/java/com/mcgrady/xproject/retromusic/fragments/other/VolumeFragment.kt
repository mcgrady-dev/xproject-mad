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
package com.mcgrady.xproject.retromusic.fragments.other

import android.graphics.Color
import android.graphics.PorterDuff
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.FragmentVolumeBinding
import com.mcgrady.xproject.retromusic.extensions.applyColor
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.retromusic.volume.AudioVolumeObserver
import com.mcgrady.xproject.retromusic.volume.OnAudioVolumeChangedListener
import com.mcgrady.xproject.theme.ThemeStore

class VolumeFragment :
    Fragment(),
    Slider.OnChangeListener,
    OnAudioVolumeChangedListener,
    View.OnClickListener {

    private var _binding: FragmentVolumeBinding? = null
    private val binding get() = _binding!!

    private var audioVolumeObserver: AudioVolumeObserver? = null

    private val audioManager: AudioManager
        get() = requireContext().getSystemService()!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentVolumeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTintable(ThemeStore.accentColor(requireContext()))
        binding.volumeDown.setOnClickListener(this)
        binding.volumeUp.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (audioVolumeObserver == null) {
            audioVolumeObserver = AudioVolumeObserver(requireActivity())
        }
        audioVolumeObserver?.register(AudioManager.STREAM_MUSIC, this)

        val audioManager = audioManager
        binding.volumeSeekBar.valueTo =
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        binding.volumeSeekBar.value =
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        binding.volumeSeekBar.addOnChangeListener(this)
    }

    override fun onAudioVolumeChanged(currentVolume: Int, maxVolume: Int) {
        if (_binding != null) {
            binding.volumeSeekBar.valueTo = maxVolume.toFloat()
            binding.volumeSeekBar.value = currentVolume.toFloat()
            binding.volumeDown.setImageResource(if (currentVolume == 0) R.drawable.ic_volume_off else R.drawable.ic_volume_down)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        audioVolumeObserver?.unregister()
        _binding = null
    }

    override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
        val audioManager = audioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value.toInt(), 0)
        setPauseWhenZeroVolume(value < 1f)
        binding.volumeDown.setImageResource(if (value == 0f) R.drawable.ic_volume_off else R.drawable.ic_volume_down)
    }

    override fun onClick(view: View) {
        val audioManager = audioManager
        when (view.id) {
            R.id.volumeDown -> audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0
            )
            R.id.volumeUp -> audioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0
            )
        }
    }

    fun tintWhiteColor() {
        val color = Color.WHITE
        binding.volumeDown.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        binding.volumeUp.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        binding.volumeSeekBar.applyColor(color)
    }

    fun setTintable(color: Int) {
        binding.volumeSeekBar.applyColor(color)
    }

    private fun setPauseWhenZeroVolume(pauseWhenZeroVolume: Boolean) {
        if (PreferenceUtil.isPauseOnZeroVolume)
            if (MusicPlayerRemote.isPlaying && pauseWhenZeroVolume)
                MusicPlayerRemote.pauseSong()
    }

    fun setTintableColor(color: Int) {
        binding.volumeDown.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        binding.volumeUp.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        // TintHelper.setTint(volumeSeekBar, color, false)
        binding.volumeSeekBar.applyColor(color)
    }

    companion object {
        fun newInstance(): VolumeFragment {
            return VolumeFragment()
        }
    }
}
