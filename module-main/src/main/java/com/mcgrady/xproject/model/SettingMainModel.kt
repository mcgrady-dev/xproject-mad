package com.mcgrady.xproject.model

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil

/**
 * Created by mcgrady on 2022/2/17.
 */
data class SettingMainModel(
    val action: Int,
    val name: String,
    val iconRes: Int,
    val desc: String? = null
) {

//    fun hasDesc(): Boolean = !desc.isNullOrEmpty()

    companion object {
        val CALLBACK: DiffUtil.ItemCallback<SettingMainModel> = object: DiffUtil.ItemCallback<SettingMainModel>() {
            override fun areItemsTheSame(
                oldItem: SettingMainModel,
                newItem: SettingMainModel
            ): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: SettingMainModel,
                newItem: SettingMainModel
            ): Boolean = true
        }
    }

    fun itemClick(view: View) {
        view.findNavController().navigate(action)
    }
}
