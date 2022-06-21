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
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class VerticalFlipTransformation : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            translationX = -position * width
            cameraDistance = 100000f

            if (position < 0.5 && position > -0.5) {
                isVisible = true
            } else {
                isInvisible = true
            }

            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 0 -> { // [-1,0]
                    alpha = 1f
                    rotationY = 180 * (1 - abs(position) + 1)
                }
                position <= 1 -> { // (0,1]
                    alpha = 1f
                    rotationY = -180 * (1 - abs(position) + 1)
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}
