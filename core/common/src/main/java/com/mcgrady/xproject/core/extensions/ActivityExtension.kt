package com.mcgrady.xproject.core.extensions

import android.app.Activity
import androidx.annotation.DimenRes


fun Activity.dip(@DimenRes id: Int): Int {
    return resources.getDimensionPixelSize(id)
}