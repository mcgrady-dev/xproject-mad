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
package com.mcgrady.xproject.theme.common.prefs.supportv7

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.widget.doAfterTextChanged
import androidx.preference.PreferenceViewHolder
import androidx.preference.SeekBarPreference
import com.mcgrady.xproject.theme.R
import com.mcgrady.xproject.theme.ThemeStore
import com.mcgrady.xproject.theme.util.ATHUtil
import com.mcgrady.xproject.theme.util.TintHelper

class ATESeekBarPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : SeekBarPreference(context, attrs, defStyleAttr, defStyleRes) {

    var unit: String = ""

    init {
        context.withStyledAttributes(attrs, R.styleable.ATESeekBarPreference, 0, 0) {
            getString(R.styleable.ATESeekBarPreference_ateKey_pref_unit)?.let {
                unit = it
            }
        }
        icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            ATHUtil.resolveColor(
                context,
                android.R.attr.colorControlNormal
            ),
            BlendModeCompat.SRC_IN
        )
    }

    override fun onBindViewHolder(view: PreferenceViewHolder) {
        super.onBindViewHolder(view)
        val seekBar = view.findViewById(R.id.seekbar) as SeekBar
        TintHelper.setTintAuto(
            seekBar, // Set MD3 accent if MD3 is enabled or in-app accent otherwise
            ThemeStore.accentColor(context), false
        )
        (view.findViewById(R.id.seekbar_value) as TextView).apply {
            appendUnit(editableText)
            doAfterTextChanged {
                appendUnit(it)
            }
        }
    }

    private fun TextView.appendUnit(editable: Editable?) {
        if (!editable.toString().endsWith(unit)) {
            append(unit)
        }
    }
}
