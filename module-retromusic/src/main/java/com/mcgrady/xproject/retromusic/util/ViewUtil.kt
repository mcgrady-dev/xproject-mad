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
package com.mcgrady.xproject.retromusic.util

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat.SRC_IN
import com.mcgrady.xproject.theme.util.ATHUtil
import com.mcgrady.xproject.theme.util.ColorUtil
import com.mcgrady.xproject.theme.util.MaterialValueHelper

object ViewUtil {

    const val RETRO_MUSIC_ANIM_TIME = 1000

    fun setProgressDrawable(progressSlider: SeekBar, newColor: Int, thumbTint: Boolean = false) {

        if (thumbTint) {
            progressSlider.thumbTintList = ColorStateList.valueOf(newColor)
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val layerDrawable = progressSlider.progressDrawable as LayerDrawable
            val progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress)
            progressDrawable.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(newColor, SRC_IN)
        } else {
            progressSlider.progressTintList = ColorStateList.valueOf(newColor)
        }
    }

    fun setProgressDrawable(progressSlider: ProgressBar, newColor: Int) {

        val layerDrawable = progressSlider.progressDrawable as LayerDrawable

        val progress = layerDrawable.findDrawableByLayerId(android.R.id.progress)
        progress.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(newColor, SRC_IN)

        val background = layerDrawable.findDrawableByLayerId(android.R.id.background)
        val primaryColor =
            ATHUtil.resolveColor(progressSlider.context, android.R.attr.windowBackground)
        background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            MaterialValueHelper.getPrimaryDisabledTextColor(
                progressSlider.context,
                ColorUtil.isColorLight(primaryColor)
            ),
            SRC_IN
        )

        val secondaryProgress = layerDrawable.findDrawableByLayerId(android.R.id.secondaryProgress)
        secondaryProgress?.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                ColorUtil.withAlpha(
                    newColor,
                    0.65f
                ),
                SRC_IN
            )
    }

    fun hitTest(v: View, x: Int, y: Int): Boolean {
        val tx = (v.translationX + 0.5f).toInt()
        val ty = (v.translationY + 0.5f).toInt()
        val left = v.left + tx
        val right = v.right + tx
        val top = v.top + ty
        val bottom = v.bottom + ty

        return x in left..right && y >= top && y <= bottom
    }

    fun convertDpToPixel(dp: Float, resources: Resources): Float {
        val metrics = resources.displayMetrics
        return dp * metrics.density
    }
}
