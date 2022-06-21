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

/**
 * @author Aidan Follestad (afollestad), Karim Abou Zeid (kabouzeid)
 */
internal interface ThemeStorePrefKeys {
    companion object {

        const val CONFIG_PREFS_KEY_DEFAULT = "[[kabouzeid_app-theme-helper]]"
        const val IS_CONFIGURED_KEY = "is_configured"
        const val IS_CONFIGURED_VERSION_KEY = "is_configured_version"
        const val VALUES_CHANGED = "values_changed"

        const val KEY_ACTIVITY_THEME = "activity_theme"

        const val KEY_PRIMARY_COLOR = "primary_color"
        const val KEY_PRIMARY_COLOR_DARK = "primary_color_dark"
        const val KEY_ACCENT_COLOR = "accent_color"
        const val KEY_STATUS_BAR_COLOR = "status_bar_color"
        const val KEY_NAVIGATION_BAR_COLOR = "navigation_bar_color"

        const val KEY_TEXT_COLOR_PRIMARY = "text_color_primary"
        const val KEY_TEXT_COLOR_PRIMARY_INVERSE = "text_color_primary_inverse"
        const val KEY_TEXT_COLOR_SECONDARY = "text_color_secondary"
        const val KEY_TEXT_COLOR_SECONDARY_INVERSE = "text_color_secondary_inverse"

        const val KEY_APPLY_PRIMARYDARK_STATUSBAR = "apply_primarydark_statusbar"
        const val KEY_APPLY_PRIMARY_NAVBAR = "apply_primary_navbar"
        const val KEY_AUTO_GENERATE_PRIMARYDARK = "auto_generate_primarydark"

        const val KEY_MATERIAL_YOU = "material_you"
        const val KEY_WALLPAPER_COLOR_LIGHT = "wallpaper_color_light"
        const val KEY_WALLPAPER_COLOR_DARK = "wallpaper_color_dark"
    }
}
