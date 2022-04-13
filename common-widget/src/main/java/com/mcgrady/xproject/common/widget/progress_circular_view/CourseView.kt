package com.mcgrady.xproject.common.widget.progress_circular_view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

/**
 * Created by mcgrady on 2022/4/7.
 */
class CourseView @JvmOverloads constructor(
    context: Context?,
    attributeSet: AttributeSet? = null,
    defStyleInt: Int = 0
) : ViewGroup(context) {


    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {

    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)

        measureChildren(widthMeasureSpec, heightMeasureSpec)


        //width = progressView.width
        //height = progressView.height

        var width = 0
        var height = 0

        for (i in 0..childCount) {
            val childView = getChildAt(i)
            width = childView.width
            height = childView.height
        }

        when (widthMode) {
            MeasureSpec.EXACTLY -> {
                setMeasuredDimension(width, height)
            }
            else -> {
                setMeasuredDimension(sizeWidth, sizeHeight)
            }
        }
    }
}