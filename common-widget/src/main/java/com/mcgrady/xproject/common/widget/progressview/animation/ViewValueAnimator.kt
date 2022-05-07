package com.mcgrady.xproject.common.widget.progressview.animation

import android.animation.ValueAnimator

interface ViewValueAnimator {

    val baseAnimator: ValueAnimator

    fun onValueUpdate(animatorInterface: IAnimatorInterface)
}