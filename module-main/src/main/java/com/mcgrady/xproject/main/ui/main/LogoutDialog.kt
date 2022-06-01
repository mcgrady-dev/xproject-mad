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
package com.mcgrady.xproject.main.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.mcgrady.xproject.main.R

/**
 * Created by mcgrady on 2022/2/22.
 */
class LogoutDialog : DialogFragment() {

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    return super.onCreateView(inflater, container, savedInstanceState)
  }

  @Suppress("UNUSED_ANONYMOUS_PARAMETER")
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    var dialog: Dialog? = null
    activity?.let {
      dialog = with(AlertDialog.Builder(it)) {
        setTitle(R.string.main_logout_title)
        setMessage(R.string.main_logout_msg)
        setPositiveButton(R.string.main_confirm) { dialog, which ->
        }
        setNegativeButton(R.string.main_cancel) { dialog, which ->
        }
        create()
      }
    }
    return dialog ?: super.onCreateDialog(savedInstanceState)
  }
}
