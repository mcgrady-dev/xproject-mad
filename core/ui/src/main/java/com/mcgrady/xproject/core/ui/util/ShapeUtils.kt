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
package com.mcgrady.xproject.core.ui.util

import android.graphics.Path

/**
 * Created by mcgrady on 2021/5/28.
 */
object ShapeUtils {

    fun roundedRect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        _rx: Float,
        _ry: Float,
        tl: Boolean = true,
        tr: Boolean = true,
        br: Boolean = true,
        bl: Boolean = true,
    ): Path {
        var rx = _rx
        var ry = _ry
        val path = Path()
        if (rx < 0) rx = 0f
        if (ry < 0) ry = 0f
        val width = right - left
        val height = bottom - top
        if (rx > width / 2) rx = width / 2
        if (ry > height / 2) ry = height / 2
        val widthMinusCorners = width - 2 * rx
        val heightMinusCorners = height - 2 * ry

        path.moveTo(right, top + ry)
        if (tr) {
            path.rQuadTo(0f, -ry, -rx, -ry) // top-right corner
        } else {
            path.rLineTo(0f, -ry)
            path.rLineTo(-rx, 0f)
        }
        path.rLineTo(-widthMinusCorners, 0f)
        if (tl) {
            path.rQuadTo(-rx, 0f, -rx, ry) // top-left corner
        } else {
            path.rLineTo(-rx, 0f)
            path.rLineTo(0f, ry)
        }
        path.rLineTo(0f, heightMinusCorners)

        if (bl) {
            path.rQuadTo(0f, ry, rx, ry) // bottom-left corner
        } else {
            path.rLineTo(0f, ry)
            path.rLineTo(rx, 0f)
        }

        path.rLineTo(widthMinusCorners, 0f)
        if (br) {
            path.rQuadTo(rx, 0f, rx, -ry) // bottom-right corner
        } else {
            path.rLineTo(rx, 0f)
            path.rLineTo(0f, -ry)
        }

        path.rLineTo(0f, -heightMinusCorners)

        path.close() // Given close, last lineto can be removed.

        return path
    }

    fun roundedRect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        _tl: Float,
        _tr: Float,
        _br: Float,
        _bl: Float,
    ): Path {
        var tl = _tl
        var tr = _tr
        var br = _br
        var bl = _bl
        val path = Path()
        if (tl < 0) tl = 0f
        if (tr < 0) tr = 0f
        if (br < 0) br = 0f
        if (bl < 0) bl = 0f
        val width = right - left
        val height = bottom - top
        val min = Math.min(width, height)
        if (tl > min / 2) tl = min / 2
        if (tr > min / 2) tr = min / 2
        if (br > min / 2) br = min / 2
        if (bl > min / 2) bl = min / 2
//        val widthMinusCorners = width - 2 * rx
//        val heightMinusCorners = height - 2 * ry
        if (tl == tr && tr == br && br == bl && tl == min / 2) {
            val radius = min / 2F
            path.addCircle(left + radius, top + radius, radius, Path.Direction.CW)
            return path
        }

        path.moveTo(right, top + tr)
        if (tr > 0) {
            path.rQuadTo(0f, -tr, -tr, -tr) // top-right corner
        } else {
            path.rLineTo(0f, -tr)
            path.rLineTo(-tr, 0f)
        }
        path.rLineTo(-(width - tr - tl), 0f)
        if (tl > 0) {
            path.rQuadTo(-tl, 0f, -tl, tl) // top-left corner
        } else {
            path.rLineTo(-tl, 0f)
            path.rLineTo(0f, tl)
        }
        path.rLineTo(0f, height - tl - bl)

        if (bl > 0) {
            path.rQuadTo(0f, bl, bl, bl) // bottom-left corner
        } else {
            path.rLineTo(0f, bl)
            path.rLineTo(bl, 0f)
        }

        path.rLineTo(width - bl - br, 0f)
        if (br > 0) {
            path.rQuadTo(br, 0f, br, -br) // bottom-right corner
        } else {
            path.rLineTo(br, 0f)
            path.rLineTo(0f, -br)
        }

        path.rLineTo(0f, -(height - br - tr))

        path.close() // Given close, last lineto can be removed.

        return path
    }
}
