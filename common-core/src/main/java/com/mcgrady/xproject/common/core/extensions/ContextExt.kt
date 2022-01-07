package com.mcgrady.xproject.common.core.extensions

import android.content.Context
import androidx.annotation.RequiresApi

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