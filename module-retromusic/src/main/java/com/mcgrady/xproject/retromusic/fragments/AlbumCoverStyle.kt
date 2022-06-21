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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mcgrady.xproject.retromusic.R

enum class AlbumCoverStyle(
    @StringRes val titleRes: Int,
    @DrawableRes val drawableResId: Int,
    val id: Int
) {
    Card(R.string.card, R.drawable.np_blur_card, 3),
    Circle(R.string.circular, R.drawable.np_circle, 2),
    Flat(R.string.flat, R.drawable.np_flat, 1),
    FullCard(R.string.full_card, R.drawable.np_adaptive, 5),
    Full(R.string.full, R.drawable.np_full, 4),
    Normal(R.string.normal, R.drawable.np_normal, 0),
}
