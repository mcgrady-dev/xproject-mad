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
package com.mcgrady.xproject.retromusic.helper

import android.content.Context
import android.view.ViewGroup
import com.mcgrady.xproject.retromusic.R

object HorizontalAdapterHelper {

    const val LAYOUT_RES = R.layout.item_image

    private const val TYPE_FIRST = 1
    private const val TYPE_MIDDLE = 2
    private const val TYPE_LAST = 3

    fun applyMarginToLayoutParams(
        context: Context,
        layoutParams: ViewGroup.MarginLayoutParams,
        viewType: Int
    ) {
        val listMargin = context.resources
            .getDimensionPixelSize(R.dimen.now_playing_top_margin)
        if (viewType == TYPE_FIRST) {
            layoutParams.leftMargin = listMargin
        } else if (viewType == TYPE_LAST) {
            layoutParams.rightMargin = listMargin
        }
    }

    fun getItemViewType(position: Int, itemCount: Int): Int {
        return when (position) {
            0 -> TYPE_FIRST
            itemCount - 1 -> TYPE_LAST
            else -> TYPE_MIDDLE
        }
    }
}
