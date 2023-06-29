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
package com.mcgrady.xproject.core.ui.progressview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

@DslMarker
internal annotation class TextFormDsl

/** creates an instance of [TextForm] from [TextForm.Builder] using kotlin dsl. */
@TextFormDsl
@JvmSynthetic
inline fun textForm(
    context: Context,
    crossinline block: TextForm.Builder.() -> Unit,
): TextForm =
    TextForm.Builder(context).apply(block).build()

/**
 * TextFrom is an attribute class what has some attributes about TextView
 * for customizing popup texts easily.
 */
class TextForm(builder: Builder) {

    val text: CharSequence? = builder.text

    @Px val textSize: Float = builder.textSize

    @ColorInt val textColor: Int = builder.textColor
    val textStyle: Int = builder.textTypeface
    val textStyleObject: Typeface? = builder.textTypefaceObject

    /** Builder class for [TextForm]. */
    @TextFormDsl
    class Builder(private val context: Context) {
        @JvmField
        @set:JvmSynthetic
        var text: CharSequence? = ""

        @JvmField @Px
        @set:JvmSynthetic
        var textSize: Float = 12f

        @JvmField @ColorInt
        @set:JvmSynthetic
        var textColor: Int = Color.WHITE

        @JvmField
        @set:JvmSynthetic
        var textTypeface: Int = Typeface.NORMAL

        @JvmField
        @set:JvmSynthetic
        var textTypefaceObject: Typeface? = null

        fun setText(value: CharSequence): Builder = apply { this.text = value }
        fun setTextResource(@StringRes value: Int): Builder = apply {
            this.text = context.getString(value)
        }

        fun setTextSize(@Px value: Float): Builder = apply { this.textSize = value }
        fun setTextColor(@ColorInt value: Int): Builder = apply { this.textColor = value }
        fun setTextColorResource(@ColorRes value: Int): Builder = apply {
            this.textColor = ContextCompat.getColor(context, value)
        }

        fun setTextTypeface(value: Int): Builder = apply { this.textTypeface = value }
        fun setTextTypeface(value: Typeface): Builder = apply { this.textTypefaceObject = value }
        fun build(): TextForm = TextForm(this)
    }
}
