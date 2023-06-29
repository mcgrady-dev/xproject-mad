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
package com.mcgrady.xproject.core.ui.parallax

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by mcgrady on 2021/6/12.
 */
open class ParallaxScrollListener(
    var decorationWidth: Float = 0f,
    var maxScaleDifference: Float = -1f,
    var listener: OnScrolledCallBack? = null,
) :
    RecyclerView.OnScrollListener() {

    private var itemWidth: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (itemWidth == 0) {
            itemWidth = recyclerView.getChildAt(0).width
        }

        val itemCount = recyclerView.adapter?.itemCount ?: 0
        val totalWidth = (itemWidth * itemCount) + (2 * decorationWidth)
        val llm = recyclerView.layoutManager as LinearLayoutManager
        val first = llm.findFirstVisibleItemPosition()
        val offset = llm.findViewByPosition(first)?.left ?: 0
//        val currentOffset = ((first * itemWidth) + (first * decorationWidth) - offset).toInt()
        val currentOffset = ((first * itemWidth) - offset)

        Log.d(
            "ParallaxScrollListener",
            """
            ParallaxScrollListener#onScrolled
            dx = $dx dy = $dy
            itemCount = $itemCount totalWidth = $totalWidth itemWidth = $itemWidth padding = $decorationWidth
            first = $first offset = $offset currentOffset = $currentOffset
            """.trimIndent(),
        )

        listener?.onScrolled(currentOffset, totalWidth, maxScaleDifference)
    }

    interface OnScrolledCallBack {

        fun onScrolled(currentOffset: Int, totalWidth: Float, maxScaleDifference: Float)
    }
}
