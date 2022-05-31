package com.mcgrady.xproject.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

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