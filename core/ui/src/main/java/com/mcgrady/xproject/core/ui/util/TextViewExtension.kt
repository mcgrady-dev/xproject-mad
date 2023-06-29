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

import android.util.TypedValue
import android.widget.TextView
import com.mcgrady.xproject.core.ui.progressview.TextForm

/** applies text form attributes to a TextView instance. */
@JvmSynthetic
internal fun TextView.applyTextForm(textForm: TextForm) {
    text = textForm.text
    setTextSize(TypedValue.COMPLEX_UNIT_SP, textForm.textSize)
    setTextColor(textForm.textColor)
    textForm.textStyleObject?.let {
        typeface = textForm.textStyleObject
    } ?: setTypeface(typeface, textForm.textStyle)
}
