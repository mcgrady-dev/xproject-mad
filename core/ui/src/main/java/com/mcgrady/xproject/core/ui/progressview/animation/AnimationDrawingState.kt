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
