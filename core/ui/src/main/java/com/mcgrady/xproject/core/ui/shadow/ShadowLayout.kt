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
package com.mcgrady.xproject.core.ui.shadow

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.mcgrady.xproject.core.ui.R
import com.mcgrady.xproject.core.ui.util.ShapeUtils

/**
 * Created by mcgrady on 2021/5/28.
 */
class ShadowLayout @JvmOverloads constructor(
    context: Context?,
    attributeSet: AttributeSet? = null,
    defStyleInt: Int = 0
) : ViewGroup(context, attributeSet, defStyleInt) {
    private val DEFAULT_CHILD_GRAVITY = Gravity.TOP or Gravity.START

    private var SIZE_UNSET = -1
    private var SIZE_DEFAULT = 0
    private var foregroundDraw: Drawable? = null
    private val selfBounds = Rect()
    private val overlayBounds = Rect()
    private var foregroundDrawGravity = Gravity.FILL
    private var foregroundDrawInPadding = true
    private var foregroundDrawBoundsChanged = false

    private val bgPaint = Paint()
    private var shadowColor: Int = 0
        set(value) {
            field = value
            updatePaintShadow(shadowRadius, shadowDx, shadowDy, value)
        }
    private var foregroundColor: Int = 0
        set(value) {
            field = value
            updateForegroundColor()
        }
    var backgroundClr: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var shadowRadius = 0f
        set(value) {
            var v = value
            if (v > getShadowMarginMax() && getShadowMarginMax() != 0f) {
                v = getShadowMarginMax()
            }
            field = value
            updatePaintShadow(v, shadowDx, shadowDy, shadowColor)
        }
        get() {
            return if (field > getShadowMarginMax() && getShadowMarginMax() != 0f) {
                getShadowMarginMax()
            } else {
                field
            }
        }
    var shadowDx = 0f
        set(value) {
            field = value
            updatePaintShadow(shadowRadius, value, shadowDy, shadowColor)
        }
    var shadowDy = 0f
        set(value) {
            field = value
            updatePaintShadow(shadowRadius, shadowDx, value, shadowColor)
        }
    var cornerRadiusTL: Float
    var cornerRadiusTR: Float
    var cornerRadiusBL: Float
    var cornerRadiusBR: Float
    var shadowMarginTop: Int = 0
        set(value) {
            field = value
            updatePaintShadow()
        }
    var shadowMarginLeft: Int = 0
        set(value) {
            field = value
            updatePaintShadow()
        }
    var shadowMarginRight: Int = 0
        set(value) {
            field = value
            updatePaintShadow()
        }
    var shadowMarginBottom: Int = 0
        set(value) {
            field = value
            updatePaintShadow()
        }

    init {
        val a = getContext().obtainStyledAttributes(
            attributeSet, R.styleable.ShadowLayout,
            defStyleInt, 0
        )
        shadowColor = a.getColor(
            R.styleable.ShadowLayout_shadowColor, Color.parseColor("#2a000000")
        )
        foregroundColor = a.getColor(
            R.styleable.ShadowLayout_foregroundColor, Color.parseColor("#1f000000")
        )
        backgroundClr = a.getColor(R.styleable.ShadowLayout_backgroundColor, Color.TRANSPARENT)
        shadowDx = a.getFloat(R.styleable.ShadowLayout_shadowDx, 0f)
        shadowDy = a.getFloat(R.styleable.ShadowLayout_shadowDy, 1f)
        shadowRadius =
            a.getDimensionPixelSize(R.styleable.ShadowLayout_shadowRadius, SIZE_DEFAULT).toFloat()
        val d = a.getDrawable(R.styleable.ShadowLayout_android_foreground)
        if (d != null) {
            foreground = d
        }
        val shadowMargin =
            a.getDimensionPixelSize(R.styleable.ShadowLayout_shadowMargin, SIZE_UNSET)
        if (shadowMargin >= 0) {
            shadowMarginTop = shadowMargin
            shadowMarginLeft = shadowMargin
            shadowMarginRight = shadowMargin
            shadowMarginBottom = shadowMargin
        } else {
            shadowMarginTop =
                a.getDimensionPixelSize(R.styleable.ShadowLayout_shadowMarginTop, SIZE_DEFAULT)
            shadowMarginLeft =
                a.getDimensionPixelSize(R.styleable.ShadowLayout_shadowMarginLeft, SIZE_DEFAULT)
            shadowMarginRight =
                a.getDimensionPixelSize(R.styleable.ShadowLayout_shadowMarginRight, SIZE_DEFAULT)
            shadowMarginBottom =
                a.getDimensionPixelSize(R.styleable.ShadowLayout_shadowMarginBottom, SIZE_DEFAULT)
        }

        val cornerRadius =
            a.getDimensionPixelSize(R.styleable.ShadowLayout_slCornerRadius, SIZE_UNSET).toFloat()
        if (cornerRadius >= 0) {
            cornerRadiusTL = cornerRadius
            cornerRadiusTR = cornerRadius
            cornerRadiusBL = cornerRadius
            cornerRadiusBR = cornerRadius
        } else {
            cornerRadiusTL =
                a.getDimensionPixelSize(R.styleable.ShadowLayout_cornerRadiusTL, SIZE_DEFAULT)
                    .toFloat()
            cornerRadiusTR =
                a.getDimensionPixelSize(R.styleable.ShadowLayout_cornerRadiusTR, SIZE_DEFAULT)
                    .toFloat()
            cornerRadiusBL =
                a.getDimensionPixelSize(R.styleable.ShadowLayout_cornerRadiusBL, SIZE_DEFAULT)
                    .toFloat()
            cornerRadiusBR =
                a.getDimensionPixelSize(R.styleable.ShadowLayout_cornerRadiusBR, SIZE_DEFAULT)
                    .toFloat()
        }
        a.recycle()
        bgPaint.color = backgroundClr
        bgPaint.isAntiAlias = true
        bgPaint.style = Paint.Style.FILL
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        setWillNotDraw(false)
        background = null
    }

    private fun updatePaintShadow() {
        updatePaintShadow(shadowRadius, shadowDx, shadowDy, shadowColor)
    }

    private fun updatePaintShadow(radius: Float, dx: Float, dy: Float, color: Int) {
        bgPaint.setShadowLayer(
            radius, dx, dy,
            color
        )
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var maxHeight = 0
        var maxWidth = 0
        var childState = 0

        setMeasuredDimension(
            getDefaultSize(0, widthMeasureSpec),
            getDefaultSize(0, heightMeasureSpec)
        )
        val shadowMeasureWidthMatchParent =
            layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT
        val shadowMeasureHeightMatchParent =
            layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT
        var widthSpec = widthMeasureSpec
        if (shadowMeasureWidthMatchParent) {
            val childWidthSize = measuredWidth - shadowMarginRight - shadowMarginLeft
            widthSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY)
        }
        var heightSpec = heightMeasureSpec
        if (shadowMeasureHeightMatchParent) {
            val childHeightSize = measuredHeight - shadowMarginTop - shadowMarginBottom
            heightSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY)
        }
        val child = getChildAt(0)
        if (child.visibility != View.GONE) {
            measureChildWithMargins(child, widthSpec, 0, heightSpec, 0)
            val lp = child.layoutParams as LayoutParams
            maxWidth =
                if (shadowMeasureWidthMatchParent)
                    Math.max(
                        maxWidth,
                        child.measuredWidth + lp.leftMargin + lp.rightMargin
                    )
                else
                    Math.max(
                        maxWidth,
                        child.measuredWidth + shadowMarginLeft + shadowMarginRight + lp.leftMargin + lp.rightMargin
                    )
            maxHeight =
                if (shadowMeasureHeightMatchParent)
                    Math.max(
                        maxHeight,
                        child.measuredHeight + lp.topMargin + lp.bottomMargin
                    )
                else
                    Math.max(
                        maxHeight,
                        child.measuredHeight + shadowMarginTop + shadowMarginBottom + lp.topMargin + lp.bottomMargin
                    )

            childState = View.combineMeasuredStates(childState, child.measuredState)
        }
        maxWidth += paddingLeft + paddingRight
        maxHeight += paddingTop + paddingBottom
        maxHeight = Math.max(maxHeight, suggestedMinimumHeight)
        maxWidth = Math.max(maxWidth, suggestedMinimumWidth)
        val drawable = foreground
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.minimumHeight)
            maxWidth = Math.max(maxWidth, drawable.minimumWidth)
        }
        setMeasuredDimension(
            View.resolveSizeAndState(
                maxWidth,
                if (shadowMeasureWidthMatchParent) widthMeasureSpec else widthSpec,
                childState
            ),
            View.resolveSizeAndState(
                maxHeight, if (shadowMeasureHeightMatchParent) heightMeasureSpec else heightSpec,
                childState shl View.MEASURED_HEIGHT_STATE_SHIFT
            )
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        layoutChildren(left, top, right, bottom, false)
        if (changed)
            foregroundDrawBoundsChanged = changed
    }

    private fun layoutChildren(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        forceLeftGravity: Boolean
    ) {
        val count = childCount

        val parentLeft = paddingLeft
        val parentRight = right - left - paddingRight

        val parentTop = paddingTop
        val parentBottom = bottom - top - paddingBottom

        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val lp = child.layoutParams as LayoutParams

                val width = child.measuredWidth
                val height = child.measuredHeight

                var childLeft = 0
                var childTop: Int

                var gravity = lp.gravity
                if (gravity == -1) {
                    gravity = DEFAULT_CHILD_GRAVITY
                }

                val layoutDirection = layoutDirection
                val absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection)
                val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK

                when (absoluteGravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
                    Gravity.CENTER_HORIZONTAL ->
                        childLeft =
                            parentLeft + (parentRight - parentLeft - width) / 2 +
                            lp.leftMargin - lp.rightMargin + shadowMarginLeft - shadowMarginRight
                    Gravity.RIGHT -> {
                        if (!forceLeftGravity) {
                            childLeft = parentRight - width - lp.rightMargin - shadowMarginRight
                        }
                    }
                    Gravity.LEFT -> {
                        childLeft = parentLeft + lp.leftMargin + shadowMarginLeft
                    }
                    else -> childLeft = parentLeft + lp.leftMargin + shadowMarginLeft
                }
                childTop = when (verticalGravity) {
                    Gravity.TOP -> parentTop + lp.topMargin + shadowMarginTop
                    Gravity.CENTER_VERTICAL ->
                        parentTop + (parentBottom - parentTop - height) / 2 +
                            lp.topMargin - lp.bottomMargin + shadowMarginTop - shadowMarginBottom
                    Gravity.BOTTOM ->
                        parentBottom - height - lp.bottomMargin - shadowMarginBottom
                    else -> parentTop + lp.topMargin + shadowMarginTop
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            val w = measuredWidth
            val h = measuredHeight
            val path = ShapeUtils.roundedRect(
                shadowMarginLeft.toFloat(),
                shadowMarginTop.toFloat(),
                (w - shadowMarginRight).toFloat(),
                (h - shadowMarginBottom).toFloat(),
                cornerRadiusTL,
                cornerRadiusTR,
                cornerRadiusBR,
                cornerRadiusBL
            )
            it.drawPath(path, bgPaint)
            canvas.clipPath(path)
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        canvas?.let {
            canvas.save()
            val w = measuredWidth
            val h = measuredHeight
            val path = ShapeUtils.roundedRect(
                shadowMarginLeft.toFloat(),
                shadowMarginTop.toFloat(),
                (w - shadowMarginRight).toFloat(),
                (h - shadowMarginBottom).toFloat(),
                cornerRadiusTL,
                cornerRadiusTR,
                cornerRadiusBR,
                cornerRadiusBL
            )
            canvas.clipPath(path)
            drawForeground(canvas)
            canvas.restore()
        }
    }

    private fun getShadowMarginMax() =
        intArrayOf(shadowMarginLeft, shadowMarginTop, shadowMarginRight, shadowMarginBottom)
            .maxOrNull()?.toFloat() ?: 0f

    private fun drawForeground(canvas: Canvas?) {
        foregroundDraw?.let {
            if (foregroundDrawBoundsChanged) {
                foregroundDrawBoundsChanged = false
                val w = right - left
                val h = bottom - top
                if (foregroundDrawInPadding) {
                    selfBounds.set(0, 0, w, h)
                } else {
                    selfBounds.set(
                        paddingLeft, paddingTop,
                        w - paddingRight, h - paddingBottom
                    )
                }
                Gravity.apply(
                    foregroundDrawGravity, it.intrinsicWidth,
                    it.intrinsicHeight, selfBounds, overlayBounds
                )
                it.bounds = overlayBounds
            }
            if (canvas != null) {
                it.draw(canvas)
            }
        }
    }

    override fun getForeground(): Drawable? {
        return foregroundDraw
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        foregroundDrawBoundsChanged = true
    }

    override fun setForegroundGravity(vaule: Int) {
        var foregroundGravity = vaule
        if (foregroundDrawGravity != foregroundGravity) {
            if (foregroundGravity and Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK == 0) {
                foregroundGravity = foregroundGravity or Gravity.START
            }
            if (foregroundGravity and Gravity.VERTICAL_GRAVITY_MASK == 0) {
                foregroundGravity = foregroundGravity or Gravity.TOP
            }
            foregroundDrawGravity = foregroundGravity
            if (foregroundDrawGravity == Gravity.FILL && foregroundDraw != null) {
                val padding = Rect()
                foregroundDraw?.getPadding(padding)
            }
            requestLayout()
        }
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === foregroundDraw
    }

    override fun jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState()
        foregroundDraw?.jumpToCurrentState()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        foregroundDraw?.takeIf { it.isStateful }?.let { it.state = drawableState }
    }

    override fun setForeground(drawable: Drawable?) {
        if (foregroundDraw != null) {
            foregroundDraw?.callback = null
            unscheduleDrawable(foregroundDraw)
        }
        foregroundDraw = drawable

        updateForegroundColor()

        if (drawable != null) {
            setWillNotDraw(false)
            drawable.callback = this
            if (drawable.isStateful) {
                drawable.state = drawableState
            }
            if (foregroundDrawGravity == Gravity.FILL) {
                val padding = Rect()
                drawable.getPadding(padding)
            }
        }
        requestLayout()
        invalidate()
    }

    private fun updateForegroundColor() {
        (foregroundDraw as RippleDrawable?)?.setColor(ColorStateList.valueOf(foregroundColor))
    }

    override fun drawableHotspotChanged(x: Float, y: Float) {
        super.drawableHotspotChanged(x, y)
        foregroundDraw?.setHotspot(x, y)
    }

    fun setShadowMargin(left: Int, top: Int, right: Int, bottom: Int) {
        shadowMarginLeft = left
        shadowMarginTop = top
        shadowMarginRight = right
        shadowMarginBottom = bottom
        requestLayout()
        invalidate()
    }

    fun setCornerRadius(tl: Float, tr: Float, br: Float, bl: Float) {
        cornerRadiusTL = tl
        cornerRadiusTR = tr
        cornerRadiusBR = br
        cornerRadiusBL = bl
        invalidate()
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return LayoutParams(lp)
    }

    class LayoutParams : MarginLayoutParams {
        var gravity = UNSPECIFIED_GRAVITY

        @SuppressLint("CustomViewStyleable")
        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
            val a = c.obtainStyledAttributes(attrs, R.styleable.ShadowLayoutParams)
            gravity = a.getInt(R.styleable.ShadowLayoutParams_layout_gravity, UNSPECIFIED_GRAVITY)
            a.recycle()
        }

        constructor(source: ViewGroup.LayoutParams) : super(source)

        companion object {
            const val UNSPECIFIED_GRAVITY = -1
        }
    }
}
