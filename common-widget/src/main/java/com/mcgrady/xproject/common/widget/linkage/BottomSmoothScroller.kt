package com.mcgrady.xproject.common.widget.linkage

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

/**
 * Created by mcgrady on 2022/4/19.
 */
class BottomSmoothScroller internal constructor(context: Context?) :
    LinearSmoothScroller(context) {
    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_END
    }

    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_END
    }
}