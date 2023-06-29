package com.mcgrady.xproject.core.ui.gradient.rainbow

import android.graphics.drawable.GradientDrawable

/** RainbowOrientation is the orientation attribute for expressing gradation. */
enum class RainbowOrientation(val value: GradientDrawable.Orientation) {
    TOP_BOTTOM(GradientDrawable.Orientation.TOP_BOTTOM),
    DIAGONAL_TOP_RIGHT(GradientDrawable.Orientation.TR_BL),
    RIGHT_LEFT(GradientDrawable.Orientation.RIGHT_LEFT),
    DIAGONAL_BOTTOM_RIGHT(GradientDrawable.Orientation.BR_TL),
    BOTTOM_TOP(GradientDrawable.Orientation.BOTTOM_TOP),
    DIAGONAL_BOTTOM_LEFT(GradientDrawable.Orientation.BL_TR),
    LEFT_RIGHT(GradientDrawable.Orientation.LEFT_RIGHT),
    DIAGONAL_TOP_LEFT(GradientDrawable.Orientation.TL_BR);

    /** gets [RainbowOrientation] using index value. */
    companion object {

        @JvmStatic
        fun get(value: Int): RainbowOrientation {
            return when (value) {
                0 -> TOP_BOTTOM
                1 -> DIAGONAL_TOP_RIGHT
                2 -> RIGHT_LEFT
                3 -> DIAGONAL_BOTTOM_RIGHT
                4 -> BOTTOM_TOP
                5 -> DIAGONAL_BOTTOM_LEFT
                6 -> LEFT_RIGHT
                else -> DIAGONAL_TOP_LEFT
            }
        }
    }
}