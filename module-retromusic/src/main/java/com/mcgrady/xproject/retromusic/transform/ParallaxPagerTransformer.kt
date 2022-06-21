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

/**
 * Created by xgc1986 on 2/Apr/2016
 */

class ParallaxPagerTransformer(private val id: Int) : ViewPager.PageTransformer {
    private var border = 0
    private var speed = 0.2f

    override fun transformPage(page: View, position: Float) {
        val parallaxView = page.findViewById<View>(id)
        page.apply {
            if (parallaxView != null) {
                if (position > -1 && position < 1) {
                    val width = parallaxView.width.toFloat()
                    parallaxView.translationX = -(position * width * speed)
                    val sc = (width - border) / width
                    if (position == 0f) {
                        scaleX = 1f
                        scaleY = 1f
                    } else {
                        scaleX = sc
                        scaleY = sc
                    }
                }
            }
        }
    }

    fun setBorder(px: Int) {
        border = px
    }

    fun setSpeed(speed: Float) {
        this.speed = speed
    }
}
