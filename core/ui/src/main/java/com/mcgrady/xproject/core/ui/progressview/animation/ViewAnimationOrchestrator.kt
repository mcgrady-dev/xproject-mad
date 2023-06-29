/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject.core.ui.progressview.animation

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
        listOf(progressAnimator),
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
        onSetupEnd: () -> Unit = {},
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
