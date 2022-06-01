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
package com.mcgrady.xproject.common.widget.parallax

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by mcgrady on 2021/6/12.
 */
class ScrollImageView @JvmOverloads constructor(
    context: Context?,
    attributeSet: AttributeSet? = null,
    defStyleInt: Int = 0
) : View(context, attributeSet, defStyleInt) {

    var image: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }

    private var offsetX = 0f
    private val paint = Paint()

    fun scroll(total: Int, fullWidth: Int, maxScaleDifference: Float) {
        image?.let {
            var scale = (it.width - measuredWidth) / (fullWidth.toFloat() - measuredWidth)
            if (maxScaleDifference >= 0) {
                scale = scale.coerceAtMost(maxScaleDifference)
            }

            offsetX = total * scale
            Log.d(TAG, "scroll: offsetX = $offsetX total = $total scale = $scale maxScaleDifference $maxScaleDifference imageWidth = ${it.width}  imageHeight = ${it.height} measuredWidth = $measuredWidth measuredHeight = $measuredHeight fullWidth $fullWidth")
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        image?.let {
            var constrainedOffsetX = offsetX
            if (0 > offsetX) {
                constrainedOffsetX = 0f
            }
            if (offsetX > it.width - measuredWidth) {
                constrainedOffsetX = (it.width - measuredWidth).toFloat()
            }

            val top = measuredHeight - it.height
            Log.d(TAG, "onDraw: constrainedOffsetX $constrainedOffsetX top = $top")

            canvas.drawBitmap(it, -constrainedOffsetX, top.toFloat(), paint)
        }
    }

    companion object {
        const val TAG = "ScrollImageView"
    }
}
