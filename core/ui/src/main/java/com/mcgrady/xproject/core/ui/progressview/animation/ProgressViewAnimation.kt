package com.mcgrady.xproject.core.ui.progressview.animation

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator

/** ProgressViewAnimation is a collection of progress animation. */
enum class ProgressViewAnimation(val value: Int) {
    NORMAL(0),
    BOUNCE(1),
    DECELERATE(2),
    ACCELERATEDECELERATE(3);

    fun getInterpolator(): Interpolator {
        return when (value) {
            BOUNCE.value -> BounceInterpolator()
            DECELERATE.value -> DecelerateInterpolator()
            ACCELERATEDECELERATE.value -> AccelerateDecelerateInterpolator()
            else -> AccelerateInterpolator()
        }
    }
}
