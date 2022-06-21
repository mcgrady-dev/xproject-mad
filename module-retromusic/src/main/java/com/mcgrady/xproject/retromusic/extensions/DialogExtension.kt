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

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mcgrady.xproject.retromusic.BuildConfig
import com.mcgrady.xproject.retromusic.R

fun Fragment.materialDialog(title: Int): MaterialAlertDialogBuilder {
    return if (BuildConfig.DEBUG) {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MaterialAlertDialogTheme
        )
    } else {
        MaterialAlertDialogBuilder(
            requireContext()
        )
    }.setTitle(title)
}

fun AlertDialog.colorButtons(): AlertDialog {
    setOnShowListener {
        getButton(AlertDialog.BUTTON_POSITIVE).accentTextColor()
        getButton(AlertDialog.BUTTON_NEGATIVE).accentTextColor()
        getButton(AlertDialog.BUTTON_NEUTRAL).accentTextColor()
    }
    return this
}

fun Fragment.materialDialog(): MaterialDialog {
    return MaterialDialog(requireContext())
        .cornerRadius(res = R.dimen.m3_dialog_corner_size)
}
