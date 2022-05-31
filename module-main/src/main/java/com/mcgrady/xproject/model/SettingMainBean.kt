package com.mcgrady.xproject.model

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil

/**
 * Created by mcgrady on 2022/2/17.
 */
data class SettingMainBean(
    val action: Int,
    val name: String,
    val iconRes: Int,
    val desc: String? = null
) {

//    fun hasDesc(): Boolean = !desc.isNullOrEmpty()

    companion object {
        val CALLBACK: DiffUtil.ItemCallback<SettingMainBean> = object: DiffUtil.ItemCallback<SettingMainBean>() {
            override fun areItemsTheSame(
                oldItem: SettingMainBean,
                newItem: SettingMainBean
            ): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: SettingMainBean,
                newItem: SettingMainBean
            ): Boolean = true
        }
    }

    fun itemClick(view: View) {
        view.findNavController().navigate(action)
    }
}
