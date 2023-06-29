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
package com.mcgrady.xproject.core.ui.util

import android.annotation.SuppressLint
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/** dp size to px size. */
@JvmSynthetic
internal fun View.dp2Px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale).toInt()
}

/** sp size to px size. */
@JvmSynthetic
internal fun View.sp2Px(sp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        context.resources.displayMetrics,
    )
}

/** px size to sp size. */
@JvmSynthetic
internal fun View.px2Sp(px: Float): Float {
    return px / resources.displayMetrics.scaledDensity
}

/** gets color from the ContextCompat. */
@JvmSynthetic
internal fun View.accentColor(): Int {
    @SuppressLint("ObsoleteSdkInt", "DiscouragedApi")
    val colorAttr: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        android.R.attr.colorAccent
    } else {
        context.resources.getIdentifier("colorAccent", "attr", context.packageName)
    }
    val outValue = TypedValue()
    context.theme.resolveAttribute(colorAttr, outValue, true)
    return outValue.data
}

/** updates [FrameLayout] params. */
@JvmSynthetic
internal fun ViewGroup.updateLayoutParams(block: ViewGroup.LayoutParams.() -> Unit) {
    layoutParams?.let {
        val params: ViewGroup.LayoutParams =
            (layoutParams as ViewGroup.LayoutParams).apply { block(this) }
        layoutParams = params
    }
}

internal const val outRangeColor = 65555

/** px size to dp size. */
internal fun View.px2Dp(px: Float): Float =
    px / (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
