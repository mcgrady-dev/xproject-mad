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
package com.mcgrady.xproject.core.ui.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.core.ui.linkage.BottomSmoothScroller
import com.mcgrady.xproject.core.ui.linkage.TopSmoothScroller

/**
 * Created by mcgrady on 2022/4/19.
 */
fun RecyclerView.smoothScrollToPosition(snapMode: Int, position: Int) {
    when (layoutManager) {
        is LinearLayoutManager -> {
            val scroller = when (snapMode) {
                LinearSmoothScroller.SNAP_TO_START -> {
                    TopSmoothScroller(context)
                }
                LinearSmoothScroller.SNAP_TO_END -> {
                    BottomSmoothScroller(context)
                }
                else -> {
                    LinearSmoothScroller(context)
                }
            }.apply {
                targetPosition = position
            }

            (layoutManager as LinearLayoutManager).startSmoothScroll(scroller)
        }
        else -> {
        }
    }
}
