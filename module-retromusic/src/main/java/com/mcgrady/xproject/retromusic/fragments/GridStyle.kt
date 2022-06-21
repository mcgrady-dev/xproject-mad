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
package com.mcgrady.xproject.retromusic.fragments

import androidx.annotation.LayoutRes
import com.mcgrady.xproject.retromusic.R

enum class GridStyle constructor(
    @param:LayoutRes @field:LayoutRes val layoutResId: Int,
    val id: Int
) {
    Grid(R.layout.item_grid, 0),
    Card(R.layout.item_card, 1),
    ColoredCard(R.layout.item_card_color, 2),
    Circular(R.layout.item_grid_circle, 3),
    Image(R.layout.image, 4),
    GradientImage(R.layout.item_image_gradient, 5)
}
