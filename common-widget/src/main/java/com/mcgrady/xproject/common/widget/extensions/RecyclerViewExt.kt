package com.mcgrady.xproject.common.widget.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.common.widget.linkage.BottomSmoothScroller
import com.mcgrady.xproject.common.widget.linkage.TopSmoothScroller

/**
 * Created by mcgrady on 2022/4/19.
 */
fun RecyclerView.smoothScrollToPosition(snapMode: Int, position: Int) {
    when (layoutManager) {
        is LinearLayoutManager -> {
            val scroller = when (snapMode) {
                LinearSmoothScroller.SNAP_TO_START -> {
                    TopSmoothScroller(context)
                }
                LinearSmoothScroller.SNAP_TO_END -> {
                    BottomSmoothScroller(context)
                }
                else -> {
                    LinearSmoothScroller(context)
                }
            }.apply {
                targetPosition = position
            }

            (layoutManager as LinearLayoutManager).startSmoothScroll(scroller)
        }
        else -> {

        }
    }
}