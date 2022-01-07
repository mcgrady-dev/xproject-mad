package com.mcgrady.xproject.utils

import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.view.WindowManager
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.Utils

/**
 * 兼容性适配工具
 * Created by mcgrady on 2021/12/8.
 */
object CompatibilityAdapterUtils {

    fun getScreenWidth(): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val wm = Utils.getApp().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm?.currentWindowMetrics.bounds.width() ?: -1
    } else {
        ScreenUtils.getScreenWidth()
    }

    fun getScreenHeight(): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val wm = Utils.getApp().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm?.currentWindowMetrics.bounds.height() ?: -1
    } else {
        ScreenUtils.getScreenHeight()
    }

    fun getAppStandbyBucket(): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val wm = Utils.getApp().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        wm?.appStandbyBucket ?: -1
    } else {
        -1
    }
}