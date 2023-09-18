package com.mcgrady.xproject.core.extensions

import androidx.core.view.WindowInsetsCompat
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.Utils
import com.mcgrady.xproject.core.extensions.InsetsExtensions.navigationBarHeight

fun WindowInsetsCompat?.getBottomInsets(): Int {
//    return if (PreferenceUtil.isFullScreenMode) {
//        return 0
//    } else {
        return this?.getInsets(WindowInsetsCompat.Type.systemBars())?.bottom ?: navigationBarHeight
//    }
}

object InsetsExtensions {
    val navigationBarHeight: Int
        get() {
            var result = 0
            val resourceId = Utils.getApp().resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = Utils.getApp().resources.getDimensionPixelSize(resourceId)
            }
            return result
        }
}