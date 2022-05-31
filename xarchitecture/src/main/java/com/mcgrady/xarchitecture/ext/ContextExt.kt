@file:Suppress("NOTHING_TO_INLINE")

package com.mcgrady.xarchitecture.ext

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

/**
 * Created by mcgrady on 2022/5/31.
 */

/**
 * Return the handle to a system-level service by class.
 *
 * The return type of this function intentionally uses a
 * [platform type](https://kotlinlang.org/docs/reference/java-interop.html#null-safety-and-platform-types)
 * to allow callers to decide whether they require a service be present or can tolerate its absence.
 *
 * @see Context.getSystemService(Class)
 */
@RequiresApi(23)
@Suppress("HasPlatformType") // Intentionally propagating platform type with unknown nullability.
inline fun <reified T> Context.systemService() = getSystemService(T::class.java)

//设置状态栏的颜色
fun Context.statusBarColor(@ColorRes colorResId: Int) {

    if (this is Activity) {
        statusBarColor(WeakReference<Activity>(this), colorResId)
    }
}

private fun Context.statusBarColor(context: WeakReference<Activity>, @ColorRes colorResId: Int) {
    context.get()?.run {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(colorResId)
        }
    }
}

// 获取 Drawable
//@kotlin.internal.InlineOnly
inline fun Context.drawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

// 获取 color
//@kotlin.internal.InlineOnly
inline fun Context.color(@ColorRes id: Int) = ContextCompat.getColor(this, id)
