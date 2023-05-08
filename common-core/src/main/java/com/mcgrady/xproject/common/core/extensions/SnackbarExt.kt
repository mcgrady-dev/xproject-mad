package com.mcgrady.xproject.common.core.extensions

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.mcgrady.xarch.extension.isNotNullOrEmpty

/**
 * Created by mcgrady on 2023/2/17.
 */

inline fun View.snackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionName: String? = null,
    noinline block: (() -> Unit?)? = null
) {
    Snackbar.make(this, message, duration).run {
        if (actionName.isNotNullOrEmpty() && block != null) {
            setAction(actionName) {
                block()
            }
        }
        show()
    }
}
inline fun View.snackbar(
    @StringRes stringResId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionName: String? = null,
    noinline block: (() -> Unit?)? = null
) {
    Snackbar.make(this, stringResId, duration).run {
        if (actionName.isNotNullOrEmpty() && block != null) {
            setAction(actionName) {
                block()
            }
        }
        show()
    }
}
