package com.mcgrady.xproject.common.widget.progress_circular_view.animation

import android.animation.ValueAnimator

interface ViewValueAnimator {

    val baseAnimator: ValueAnimator

    fun onValueUpdate(animatorInterface: IAnimatorInterface)
}