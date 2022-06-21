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
package com.mcgrady.xproject.retromusic.extensions

import android.app.Activity
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.dimToPixel(@DimenRes dimenRes: Int): Int {
    return resources.getDimensionPixelSize(dimenRes)
}

fun Activity.dipToPix(dpInFloat: Float): Float {
    val scale = resources.displayMetrics.density
    return dpInFloat * scale + 0.5f
}

fun Fragment.dipToPix(dpInFloat: Float): Float {
    val scale = resources.displayMetrics.density
    return dpInFloat * scale + 0.5f
}
