package com.mcgrady.xproject.core.extensions

import android.content.Context
import android.graphics.Color
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils

//@ColorInt
//fun Context.darkAccentColor(): Int {
//    return ColorUtils.blendARGB(
//        accentColor(),
//        surfaceColor(),
//        if (surfaceColor().isColorLight) 0.9f else 0.92f
//    )
//}
//
//fun Context.accentColor() = ColorExtension.accentColor(this)
//
//object ColorExtension {
//    @CheckResult
//    @ColorInt
//    fun accentColor(context: Context): Int {
//        // Set MD3 accent if MD3 is enabled or in-app accent otherwise
//        if (isMD3Enabled(context) && VersionUtils.hasS()) {
//            return ContextCompat.getColor(context, R.color.m3_accent_color)
//        }
//        val desaturatedColor = prefs(context).getBoolean("desaturated_color", false)
//        val color = if (isWallpaperAccentEnabled(context)) {
//            wallpaperColor(context, isWindowBackgroundDark(context))
//        } else {
//            prefs(context).getInt(
//                ThemeStorePrefKeys.KEY_ACCENT_COLOR,
//                resolveColor(context, androidx.appcompat.R.attr.colorAccent, Color.parseColor("#263238"))
//            )
//        }
//        return if (isWindowBackgroundDark(context) && desaturatedColor) ColorUtil.desaturateColor(
//            color,
//            0.4f
//        ) else color
//    }
//}

