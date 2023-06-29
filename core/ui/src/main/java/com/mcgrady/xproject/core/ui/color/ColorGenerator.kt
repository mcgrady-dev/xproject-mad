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
package com.mcgrady.xproject.core.ui.color

import java.util.Random
import kotlin.math.abs

/**
 * Created by mcgrady on 2021/11/11.
 */
class ColorGenerator private constructor(private val colors: List<Int>) {
    companion object {
        var DEFAULT: ColorGenerator? = null
        var MATERIAL: ColorGenerator? = null
        fun create(colorList: List<Int>): ColorGenerator {
            return ColorGenerator(colorList)
        }

        init {
            DEFAULT = create(
                listOf(
                    -0xe9c9c,
                    -0xa7aa7,
                    -0x65bc2,
                    -0x1b39d2,
                    -0x98408c,
                    -0xa65d42,
                    -0xdf6c33,
                    -0x529d59,
                    -0x7fa87f,
                ),
            )
            MATERIAL = create(
                listOf(
                    -0x1a8c8d,
                    -0xf9d6e,
                    -0x459738,
                    -0x6a8a33,
                    -0x867935,
                    -0x9b4a0a,
                    -0xb03c09,
                    -0xb22f1f,
                    -0xb24954,
                    -0x7e387c,
                    -0x512a7f,
                    -0x759b,
                    -0x2b1ea9,
                    -0x2ab1,
                    -0x48b3,
                    -0x5e7781,
                    -0x6f5b52,
                ),
            )
        }
    }

    private val random: Random = Random(System.currentTimeMillis())
    val randomColor: Int
        get() = colors[random.nextInt(colors.size)]

    fun getColor(key: Any): Int {
        return colors[abs(key.hashCode()) % colors.size]
    }
}
