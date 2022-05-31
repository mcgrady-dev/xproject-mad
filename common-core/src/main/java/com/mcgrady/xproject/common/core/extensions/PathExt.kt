@file:Suppress("NOTHING_TO_INLINE")

package com.mcgrady.xproject.common.core.extensions

import android.content.Context

/**
 * Created by mcgrady on 2022/5/31.
 */

inline fun Context.internalAppCachePath(): String = cacheDir.absolutePath ?: ""