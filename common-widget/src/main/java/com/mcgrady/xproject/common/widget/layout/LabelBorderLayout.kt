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
package com.mcgrady.xproject.common.widget.layout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Shader
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import com.mcgrady.xproject.common.widget.R
import com.mcgrady.xproject.common.widget.UIUtil

/**
 * Created by mcgrady on 2021/5/26.
 */
class LabelBorderLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    private val defStyleRes: Int = 0
) : ConstraintLayout(
    context,
    attributeSet,
    defStyleRes
) {
    private val path = Path()

    private var cornerRadius = 8f

    private var labelTextHeight = 0f
    private var labelTextWidth = 0f

    private var labelBitmap: Bitmap? = null

    private var labelTextSize = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        14f,
        context.resources.displayMetrics
    )

    private var borderColor: Int = Color.TRANSPARENT
    private var borderColorStart: Int = -1
    private var borderColorEnd: Int = -1

    private var labelTextColor = Color.TRANSPARENT

    private var labelText: String? = ""
    private var labelImageRes: Int = -1
    private var labelImageSize = 0f
    private var labelPadding = 10.0f

    private val borderGapPoint = PointF(60.0f, 0.0f)

    private var spacing = UIUtil.dp2px(context, 3f)

    private val paint = Paint(ANTI_ALIAS_FLAG).apply {
        color = borderColor
        style = Paint.Style.STROKE
        strokeWidth = 8f
        textSize = labelTextSize
    }

    private var linearGradient: LinearGradient? = null

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        attributeSet?.let(::initAttrs)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed) {
            if (borderColorStart != -1 && borderColorEnd != -1 && width > 0 && height > 0) {
                linearGradient = LinearGradient(
                    spacing.toFloat(),
                    0f,
                    width.toFloat(),
                    height.toFloat(),
                    borderColorStart,
                    borderColorEnd,
                    Shader.TileMode.MIRROR
                )
            }

            updateBorderPath(labelImageSize.plus(labelPadding).plus(labelTextWidth))
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (isInEditMode) {
            return
        }

        paint.shader = null
        paint.style = Paint.Style.FILL
        if (labelBitmap != null) {
            /*
            val src = Rect(0, 0, labelBitmap!!.width, labelBitmap!!.height)
            val dst = Rect(
                borderGapPoint.x.toInt(),
                paint.strokeWidth.div(2).toInt(),
                labelImageSize.toInt() + borderGapPoint.x.toInt(),
                labelImageSize.toInt()
            )
            canvas.drawBitmap(labelBitmap!!, src, dst, null)
            */

            // FIXME 图片资源未适配情况下，会按资源大小进行绘制 by mcgrady
            canvas.drawBitmap(
                labelBitmap!!,
                borderGapPoint.x,
                paint.strokeWidth.div(2),
                null
            )
        }

        paint.color = labelTextColor
        if (!TextUtils.isEmpty(labelText)) {
            canvas.drawText(
                labelText!!,
                labelImageSize.plus(borderGapPoint.x).plus(labelPadding),
                labelImageSize.plus(borderGapPoint.y).div(2).coerceAtLeast(borderGapPoint.y),
                paint
            )
        }

        paint.style = Paint.Style.FILL_AND_STROKE
        if (linearGradient != null) {
            paint.shader = linearGradient
            paint.style = Paint.Style.STROKE
            canvas.drawPath(path, paint)
        } else {
            paint.color = borderColor
            paint.style = Paint.Style.STROKE
            canvas.drawPath(path, paint)
        }
    }

    private fun initAttrs(attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.LabelBorderLayout,
            defStyleRes,
            0
        )
        with(typedArray) {
            borderColorStart = getColor(R.styleable.LabelBorderLayout_borderColorStart, -1)
            borderColorEnd = getColor(R.styleable.LabelBorderLayout_borderColorEnd, -1)

            borderColor = getColor(R.styleable.LabelBorderLayout_borderColor, paint.color)
            paint.color = borderColor

            paint.strokeWidth = getDimension(
                R.styleable.LabelBorderLayout_borderWidth,
                paint.strokeWidth
            )

            labelTextSize = getDimension(
                R.styleable.LabelBorderLayout_labelTextSize,
                paint.textSize
            ).also { paint.textSize = it }
            labelTextColor =
                getColor(R.styleable.LabelBorderLayout_labelTextColor, Color.TRANSPARENT)

            cornerRadius = getDimension(
                R.styleable.LabelBorderLayout_cornerRadius,
                cornerRadius
            )
            labelText = getString(R.styleable.LabelBorderLayout_labelText)
            labelImageRes = getResourceId(R.styleable.LabelBorderLayout_labelImage, -1)
            labelImageSize = getDimension(R.styleable.LabelBorderLayout_labelImageSize, 0f)

            if (labelImageRes != -1) {
                labelBitmap = getDrawable(labelImageRes)!!.toBitmap()
            }

            if (!TextUtils.isEmpty(labelText)) {
                val rect = Rect()
                paint.getTextBounds(
                    labelText,
                    0,
                    labelText!!.length,
                    rect
                )

                labelTextHeight = rect.height().toFloat()
                labelTextWidth = rect.width().plus(spacing.times(2)).toFloat()

                setPadding(
                    paint.strokeWidth.toInt(),
                    labelImageSize.coerceAtLeast(labelTextHeight).toInt(),
                    paint.strokeWidth.toInt(),
                    paint.strokeWidth.toInt()
                )
            }

//            borderGapPoint.y = Math.max(labelImageSize, labelTextHeight)
            borderGapPoint.y = labelTextHeight
        }
    }

    private fun updateBorderPath(
        textWidth: Float
    ) {
        path.close()
        path.reset()
        val strokeHalf = paint.strokeWidth.div(2)
        path.apply {

            val offsetY = labelImageSize.div(2).coerceAtLeast(labelTextHeight.div(2))

            moveTo(
                borderGapPoint.x.minus(spacing).minus(labelPadding.div(2)),
                strokeHalf.plus(offsetY)
            )

            lineTo(
                cornerRadius,
                strokeHalf.plus(offsetY)
            )

            quadTo(
                strokeHalf,
                strokeHalf.plus(offsetY), strokeHalf, cornerRadius.plus(offsetY)
            )

            lineTo(
                strokeHalf,
                height.minus(strokeHalf + cornerRadius)
            )

            quadTo(
                strokeHalf,
                height.minus(strokeHalf),
                cornerRadius,
                height.minus(strokeHalf)
            )

            lineTo(
                width.minus(cornerRadius),
                height.minus(strokeHalf)
            )

            quadTo(
                width.minus(strokeHalf),
                height.minus(strokeHalf),
                width.minus(strokeHalf),
                height.minus(cornerRadius)
            )

            lineTo(
                width.minus(strokeHalf),
                strokeHalf.plus(offsetY).plus(cornerRadius)
            )

            quadTo(
                width.minus(strokeHalf),
                strokeHalf.plus(offsetY),
                width.minus(cornerRadius),
                strokeHalf.plus(offsetY)
            )

            lineTo(
                borderGapPoint.x.plus(textWidth),
                strokeHalf.plus(offsetY)
            )
        }
    }
}
