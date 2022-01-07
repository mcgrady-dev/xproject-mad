package com.mcgrady.xproject.testing.test.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.roundToInt

/**
 * Created by mcgrady on 2021/12/25.
 */
class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var lastY: Float? = 0F
    private var lastX: Float? = 0F

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x ?: 0F
        val y = event?.y ?: 0F

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetX = x.minus(lastX ?: 0F).roundToInt()
                val offsetY = y.minus(lastY ?: 0F).roundToInt()

//                layout(
//                    left + offsetX, top + offsetY,
//                    right + offsetX, bottom + offsetY
//                )

//                offsetLeftAndRight(offsetX)
//                offsetTopAndBottom(offsetY)

                var lp = (layoutParams as ConstraintLayout.LayoutParams).apply {
                    leftMargin = left + offsetX
                    topMargin = top + offsetY
                }
                layoutParams = lp

            }
            else -> {}
        }
        return true
    }
}
