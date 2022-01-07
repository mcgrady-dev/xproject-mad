package com.mcgrady.xproject.common_widget.progress_circular_view.animation

import android.animation.AnimatorSet

/**
 * @param setupAnimators These animators will run before the progressAnimators.
 * When stopping the animations, these animators will be played in reverse, to animate back to the original state.
 * It's really useful to expand/collapse the arches, manipulating the [AnimationDrawingState.archesExpansionProgress] parameter during animation
 * @param progressAnimators These will e run after the setup animators.
 * They will not be called in reverse, as they expected to repeat infinitely.
 */
class ViewAnimationOrchestrator(
    private val setupAnimators: List<ViewValueAnimator> = listOf(),
    private val progressAnimators: List<ViewValueAnimator> = listOf(),
) {

    constructor(setupAnimator: ViewValueAnimator, progressAnimator: ViewValueAnimator) : this(
        listOf(setupAnimator),
        listOf(progressAnimator)
    )

    private val setupSet = AnimatorSet().apply {
        val baseAnimators = setupAnimators.map { it.baseAnimator }
        if (baseAnimators.isNotEmpty()) {
            playTogether(*baseAnimators.toTypedArray())
        }
    }

    /**
     * These animators will be called
     */
    private val progressSet = AnimatorSet().apply {
        val baseAnimators = progressAnimators.map { it.baseAnimator }
        if (baseAnimators.isNotEmpty()) {
            playTogether(*baseAnimators.toTypedArray())
        }
    }

    private val animatorSet = AnimatorSet().apply {
        playSequentially(setupSet, progressSet)
    }

    internal fun cancel() {
        animatorSet.cancel()
    }

    internal fun start() {
        animatorSet.start()
    }

    internal fun reverse() {
        setupAnimators.forEach { animator ->
            animator.baseAnimator.reverse()
        }
    }

    internal fun attach(
        animatorInterface: IAnimatorInterface,
        onSetupEnd: () -> Unit = {}
    ) {
        (setupAnimators + progressAnimators).forEach { animator ->
            animator.baseAnimator.addUpdateListener {
                animator.onValueUpdate(animatorInterface)
            }
        }
        setupSet.addOnAnimationEndListener {
            onSetupEnd()
        }
    }
}