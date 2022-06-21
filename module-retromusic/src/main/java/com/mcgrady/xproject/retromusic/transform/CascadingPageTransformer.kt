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
package com.mcgrady.xproject.retromusic.transform

import android.view.View
import androidx.viewpager.widget.ViewPager

class CascadingPageTransformer : ViewPager.PageTransformer {

    private var mScaleOffset = 40

    override fun transformPage(page: View, position: Float) {
        page.apply {
            when {
                position < -1 -> { // [-Infinity,-1)
                    alpha = 0f
                }
                position <= 0 -> {
                    alpha = 1f
                    rotation = 45 * position
                    translationX = width / 3 * position
                }
                else -> {
                    alpha = 1f
                    rotation = 0f
                    val scale = (width - mScaleOffset * position) / width.toFloat()

                    scaleX = scale
                    scaleY = scale

                    translationX = -width * position
                    translationY = mScaleOffset * 0.8f * position
                }
            }
        }
    }
}
