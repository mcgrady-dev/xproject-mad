package com.mcgrady.xproject.common_widget.progress_circular_view.animation

/**
 * Created by mcgrady on 2021/12/16.
 */
data class AnimationDrawingState(
    private val archesExpansionProgress: Float,
    private val rotationProgress: Float,
) {
    val coercedArchesExpansionProgress: Float =
        archesExpansionProgress.coerceIn(MIN_VALUE, MAX_VALUE)
    val coercedRotationProgress: Float = rotationProgress.coerceIn(MIN_VALUE, MAX_VALUE)

    companion object {
        const val MAX_VALUE = 1f
        const val MIN_VALUE = 0f
    }
}
