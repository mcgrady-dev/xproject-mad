package com.mcgrady.xproject.common.widget.progress_circular_view.animation

/**
 * Created by mcgrady on 2021/12/16.
 */
interface IAnimatorInterface {

    fun updateAnimationState(update: (currentState: AnimationDrawingState) -> AnimationDrawingState)
}