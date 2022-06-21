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
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class HingeTransformation : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            translationX = -position * width
            pivotX = 0f
            pivotY = 0f

            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                    // The Page is off-screen but it may still interfere with
                    // click events of current page if
                    // it's visibility is not set to Gone
                    isVisible = false
                }
                position <= 0 -> { // [-1,0]
                    rotation = 90 * abs(position)
                    alpha = 1 - abs(position)
                    isVisible = true
                }
                position <= 1 -> { // (0,1]
                    rotation = 0f
                    alpha = 1f
                    isVisible = true
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                    isVisible = false
                }
            }
        }
    }
}
