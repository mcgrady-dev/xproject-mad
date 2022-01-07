package com.mcgrady.xproject.common_widget.parallax

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
class ScrollImageView : View {
    var image: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }

    private var offsetX = 0f
    private var paint: Paint = Paint()

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    fun scroll(total: Int, fullWidth: Int, maxScaleDifference: Float) {
        if (image == null) {
            return
        }

        var scale = (image!!.width - measuredWidth) / (fullWidth.toFloat() - measuredWidth)
        if (maxScaleDifference >= 0) {
            scale = Math.min(scale, maxScaleDifference)
        }

        offsetX = (total * scale)
        Log.d("ScrollImageView", "scroll: offsetX = $offsetX total = $total scale = $scale maxScaleDifference $maxScaleDifference imageWidth = ${image!!.width}  imageHeight = ${image!!.height} measuredWidth = $measuredWidth measuredHeight = $measuredHeight fullWidth $fullWidth")
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (image == null) {
            return
        }

        var constrainedOffsetX = offsetX
        if (0 > offsetX) {
            constrainedOffsetX = 0f
        }
        if (offsetX > image!!.width - measuredWidth) {
            constrainedOffsetX = (image!!.width - measuredWidth).toFloat()
        }

        val top = measuredHeight - image!!.height
        Log.d("ScrollImageView", "onDraw: constrainedOffsetX $constrainedOffsetX top = $top")

        canvas.drawBitmap(image!!, -constrainedOffsetX, top.toFloat(), paint)
    }
}