package com.mcgrady.xproject.common.widget.progressview.animation

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

object DefaultAnimationOrchestrator {

    private const val DEFAULT_ROTATION_DURATION = 2000L
    private const val DEFAULT_EXPANSION_DURATION = 250L

    fun create(
        rotationDurationInMillis: Long = DEFAULT_ROTATION_DURATION,
        expandDurationInMillis: Long = DEFAULT_EXPANSION_DURATION
    ): ViewAnimationOrchestrator {
        val expansionAnimator = createDefaultExpansionAnimator(expandDurationInMillis)
        val rotationAnimator = createDefaultRotationAnimator(rotationDurationInMillis)

        return ViewAnimationOrchestrator(expansionAnimator, rotationAnimator)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun createDefaultExpansionAnimator(expandDurationInMillis: Long): ViewValueAnimator {
        return object : ViewValueAnimator {
            override val baseAnimator = ValueAnimator.ofFloat(
                AnimationDrawingState.MIN_VALUE,
                AnimationDrawingState.MAX_VALUE
            ).apply {
                interpolator = LinearInterpolator()
                duration = expandDurationInMillis
            }

            override fun onValueUpdate(animatorInterface: IAnimatorInterface) {
                animatorInterface.updateAnimationState { state ->
                    val animatedValue = baseAnimator.animatedValue as Float
                    state.copy(archesExpansionProgress = animatedValue)
                }
            }
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun createDefaultRotationAnimator(rotationDurationInMillis: Long): ViewValueAnimator {
        return object : ViewValueAnimator {
            override val baseAnimator: ValueAnimator = ValueAnimator.ofFloat(
                AnimationDrawingState.MIN_VALUE,
                AnimationDrawingState.MAX_VALUE
            ).apply {
                repeatCount = ValueAnimator.INFINITE
                duration = rotationDurationInMillis
                interpolator = LinearInterpolator()
            }

            override fun onValueUpdate(animatorInterface: IAnimatorInterface) {
                animatorInterface.updateAnimationState { state ->
                    val animatedValue = baseAnimator.animatedValue as Float
                    state.copy(rotationProgress = animatedValue)
                }
            }
        }
    }
}