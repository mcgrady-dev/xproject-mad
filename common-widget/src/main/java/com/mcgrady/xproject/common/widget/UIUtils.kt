package com.mcgrady.xproject.common.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.util.TypedValue
import android.view.View

/**
 * Created by mcgrady on 2021/12/16.
 */
object UIUtils {

    @JvmStatic
    fun dp2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    @JvmStatic
    fun px2dp(pxValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    @JvmStatic
    fun sp2px(spValue: Float): Int {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    @JvmStatic
    fun px2sp(pxValue: Float): Int {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    @JvmStatic
    fun dp2px(context: Context, value: Float): Int {
        return (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            context.resources.displayMetrics
        ) + 0.5f).toInt()
    }

    @JvmStatic
    fun sp2px(context: Context, value: Float): Int {
        return (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            value,
            context.resources.displayMetrics
        ) + 0.5f).toInt()
    }

    fun measure(measureSpec: Int, defaultSize: Int): Int {
        var result = defaultSize
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize)
        }
        return result
    }

    /**
     * 测量文字高度
     * @param paint
     * @return
     */
    fun measureTextHeight(paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return Math.abs(fontMetrics.ascent) - fontMetrics.descent
    }
}