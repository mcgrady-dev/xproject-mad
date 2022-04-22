package com.mcgrady.xproject.common.widget.linkage

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

/**
 * Created by mcgrady on 2022/4/19.
 */
class TopSmoothScroller internal constructor(context: Context?) :
    LinearSmoothScroller(context) {
    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_START
    }

    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_START
    }
}