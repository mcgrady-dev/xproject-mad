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
package com.mcgrady.xproject.core.ui.edittext

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.mcgrady.xproject.core.ui.R
import com.mcgrady.xproject.core.ui.util.SizeUtils.dp2px

/**
 *
 * @author: mcgrady
 * @date: 2018/12/20
 */
class ClearEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr),
    View.OnTouchListener,
    View.OnFocusChangeListener,
    TextWatcher {

    private var drawableClear: Drawable? = null
    private var clearListener: OnClearListener? = null

    init {
        initialize(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialize(context: Context) {
        ContextCompat.getDrawable(context, R.mipmap.common_ic_search_clear)?.let {
            // Wrap the drawable so that it can be tinted pre Lollipop
            // DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
            drawableClear = DrawableCompat.wrap(it).apply {
                setBounds(0, 0, dp2px(16f), dp2px(16f))
            }
        }
        setClearIconVisible(false)
        super.setOnTouchListener(this)
        super.addTextChangedListener(this)
//        ViewCompat.setBackgroundTintList(this, ContextCompat.getColorStateList(context, R.color.picture_color_transparent_e0db));
    }

    private fun setClearIconVisible(visible: Boolean) {
        drawableClear?.alpha = if (visible) 255 else 0
        val drawables = compoundDrawables
        setCompoundDrawables(
            drawables[0],
            drawables[1],
            drawableClear,
            drawables[3]
        )
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        setClearIconVisible(hasFocus && (text?.length ?: 0) > 0)
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (drawableClear?.alpha == 255 &&
            motionEvent.x.toInt() > (width - paddingRight - (drawableClear?.intrinsicWidth ?: 0))
        ) {
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                setText("")
            }

            clearListener?.clear()

            return true
        }

        return false
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (isFocused) {
            setClearIconVisible(s.isNotEmpty())
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable) {}

    fun setOnClearListener(clearListener: OnClearListener?) {
        this.clearListener = clearListener
    }

    interface OnClearListener {
        fun clear()
    }
}
