/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject.retromusic.extensions

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.PowerManager
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar
import com.mcgrady.xproject.retromusic.util.PreferenceUtil

fun Fragment.getIntRes(@IntegerRes int: Int): Int {
    return resources.getInteger(int)
}

fun Context.getIntRes(@IntegerRes int: Int): Int {
    return resources.getInteger(int)
}

val Context.generalThemeValue
    get() = PreferenceUtil.getGeneralThemeValue(isSystemDarkModeEnabled())

fun Context.isSystemDarkModeEnabled(): Boolean {
    val isBatterySaverEnabled =
        (getSystemService<PowerManager>())?.isPowerSaveMode ?: false
    val isDarkModeEnabled =
        (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    return isBatterySaverEnabled or isDarkModeEnabled
}

inline fun <reified T : Any> Fragment.extra(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    if (value is T) value else default
}

inline fun <reified T : Any> Fragment.extraNotNull(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}

fun AppCompatActivity.currentFragment(navHostId: Int): Fragment? {
    val navHostFragment: NavHostFragment =
        supportFragmentManager.findFragmentById(navHostId) as NavHostFragment
    return navHostFragment.childFragmentManager.fragments.firstOrNull()
}

@Suppress("UNCHECKED_CAST")
fun <T> AppCompatActivity.whichFragment(@IdRes id: Int): T {
    return supportFragmentManager.findFragmentById(id) as T
}

@Suppress("UNCHECKED_CAST")
fun <T> Fragment.whichFragment(@IdRes id: Int): T {
    return childFragmentManager.findFragmentById(id) as T
}

fun Fragment.showToast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    showToast(getString(stringRes), duration)
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable {
    return AppCompatResources.getDrawable(this, drawableRes)!!
}

fun Fragment.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable {
    return AppCompatResources.getDrawable(requireContext(), drawableRes)!!
}

fun Fragment.applyToolbar(toolbar: MaterialToolbar) {
    (requireActivity() as AppCompatActivity).applyToolbar(toolbar)
}

fun Fragment.dip(@DimenRes id: Int): Int {
    return resources.getDimensionPixelSize(id)
}
