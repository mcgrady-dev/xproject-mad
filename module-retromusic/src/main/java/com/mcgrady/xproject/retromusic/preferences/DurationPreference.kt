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
package com.mcgrady.xproject.retromusic.preferences

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat.SRC_IN
import androidx.fragment.app.DialogFragment
import com.google.android.material.slider.Slider
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.PreferenceDialogAudioFadeBinding
import com.mcgrady.xproject.retromusic.extensions.addAccentColor
import com.mcgrady.xproject.retromusic.extensions.colorButtons
import com.mcgrady.xproject.retromusic.extensions.colorControlNormal
import com.mcgrady.xproject.retromusic.extensions.materialDialog
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.theme.common.prefs.supportv7.ATEDialogPreference

class DurationPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ATEDialogPreference(context, attrs, defStyleAttr, defStyleRes) {
    init {
        icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            context.colorControlNormal(),
            SRC_IN
        )
    }
}

class DurationPreferenceDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = PreferenceDialogAudioFadeBinding.inflate(layoutInflater)

        binding.slider.apply {
            addAccentColor()
            value = PreferenceUtil.audioFadeDuration.toFloat()
            updateText(value.toInt(), binding.duration)
            addOnChangeListener(
                Slider.OnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        updateText(value.toInt(), binding.duration)
                    }
                }
            )
        }

        return materialDialog(R.string.audio_fade_duration)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.save) { _, _ -> updateDuration(binding.slider.value.toInt()) }
            .setView(binding.root)
            .create()
            .colorButtons()
    }

    private fun updateText(value: Int, duration: TextView) {
        var durationText = "$value ms"
        if (value == 0) durationText += " / Off"
        duration.text = durationText
    }

    private fun updateDuration(duration: Int) {
        PreferenceUtil.audioFadeDuration = duration
    }

    companion object {
        fun newInstance(): DurationPreferenceDialog {
            return DurationPreferenceDialog()
        }
    }
}
