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
package com.mcgrady.xproject.theme

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes

/**
 * @author Aidan Follestad (afollestad), Karim Abou Zeid (kabouzeid)
 */
internal interface ThemeStoreInterface {

    // Activity theme

    fun activityTheme(@StyleRes theme: Int): ThemeStore

    // Primary colors

    fun primaryColor(@ColorInt color: Int): ThemeStore

    fun primaryColorRes(@ColorRes colorRes: Int): ThemeStore

    fun primaryColorAttr(@AttrRes colorAttr: Int): ThemeStore

    fun autoGeneratePrimaryDark(autoGenerate: Boolean): ThemeStore

    fun primaryColorDark(@ColorInt color: Int): ThemeStore

    fun primaryColorDarkRes(@ColorRes colorRes: Int): ThemeStore

    fun primaryColorDarkAttr(@AttrRes colorAttr: Int): ThemeStore

    // Accent colors

    fun accentColor(@ColorInt color: Int): ThemeStore

    fun wallpaperColor(context: Context, color: Int): ThemeStore

    fun accentColorRes(@ColorRes colorRes: Int): ThemeStore

    fun accentColorAttr(@AttrRes colorAttr: Int): ThemeStore

    // Status bar color

    fun statusBarColor(@ColorInt color: Int): ThemeStore

    fun statusBarColorRes(@ColorRes colorRes: Int): ThemeStore

    fun statusBarColorAttr(@AttrRes colorAttr: Int): ThemeStore

    // Navigation bar color

    fun navigationBarColor(@ColorInt color: Int): ThemeStore

    fun navigationBarColorRes(@ColorRes colorRes: Int): ThemeStore

    fun navigationBarColorAttr(@AttrRes colorAttr: Int): ThemeStore

    // Primary text color

    fun textColorPrimary(@ColorInt color: Int): ThemeStore

    fun textColorPrimaryRes(@ColorRes colorRes: Int): ThemeStore

    fun textColorPrimaryAttr(@AttrRes colorAttr: Int): ThemeStore

    fun textColorPrimaryInverse(@ColorInt color: Int): ThemeStore

    fun textColorPrimaryInverseRes(@ColorRes colorRes: Int): ThemeStore

    fun textColorPrimaryInverseAttr(@AttrRes colorAttr: Int): ThemeStore

    // Secondary text color

    fun textColorSecondary(@ColorInt color: Int): ThemeStore

    fun textColorSecondaryRes(@ColorRes colorRes: Int): ThemeStore

    fun textColorSecondaryAttr(@AttrRes colorAttr: Int): ThemeStore

    fun textColorSecondaryInverse(@ColorInt color: Int): ThemeStore

    fun textColorSecondaryInverseRes(@ColorRes colorRes: Int): ThemeStore

    fun textColorSecondaryInverseAttr(@AttrRes colorAttr: Int): ThemeStore

    // Toggle configurations

    fun coloredStatusBar(colored: Boolean): ThemeStore

    fun coloredNavigationBar(applyToNavBar: Boolean): ThemeStore

    // Commit/apply

    fun commit()
}
