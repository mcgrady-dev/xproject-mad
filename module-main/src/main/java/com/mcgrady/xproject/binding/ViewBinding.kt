package com.mcgrady.xproject.binding

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Created by mcgrady on 2022/2/18.
 */
object ViewBinding {

    @JvmStatic
    @BindingAdapter("goneUnless")
    fun goneUnless(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }
}