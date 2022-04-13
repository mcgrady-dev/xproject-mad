package com.mcgrady.xproject.util

import android.view.View
import androidx.navigation.findNavController


/**
 * Created by mcgrady on 2022/2/22.
 */
class SettingHandlers {

    fun settingItemClick(view: View, action: Int) {
        view.findNavController().navigate(action)
    }
}