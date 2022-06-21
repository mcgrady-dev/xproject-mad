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
package com.mcgrady.xproject.retromusic.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.ItemPermissionBinding
import com.mcgrady.xproject.retromusic.extensions.accentOutlineColor
import com.mcgrady.xproject.theme.ThemeStore
import com.mcgrady.xproject.theme.util.ColorUtil

class PermissionItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var binding: ItemPermissionBinding
    val checkImage get() = binding.checkImage

    init {
        binding = ItemPermissionBinding.inflate(LayoutInflater.from(context), this, true)

        context.withStyledAttributes(attrs, R.styleable.PermissionItem, 0, 0) {
            binding.title.text = getText(R.styleable.PermissionItem_permissionTitle)
            binding.summary.text = getText(R.styleable.PermissionItem_permissionTitleSubTitle)
            binding.number.text = getText(R.styleable.PermissionItem_permissionTitleNumber)
            binding.button.text = getText(R.styleable.PermissionItem_permissionButtonTitle)
            binding.button.setIconResource(
                getResourceId(
                    R.styleable.PermissionItem_permissionIcon,
                    R.drawable.ic_album
                )
            )
            val color = ThemeStore.accentColor(context)
            binding.number.backgroundTintList =
                ColorStateList.valueOf(ColorUtil.withAlpha(color, 0.22f))

            if (!isInEditMode) binding.button.accentOutlineColor()
        }
    }

    fun setButtonClick(callBack: () -> Unit) {
        binding.button.setOnClickListener { callBack() }
    }

    fun setNumber(number: String) {
        binding.number.text = number
    }
}
