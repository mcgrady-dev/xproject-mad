package com.mcgrady.xproject.ui.adapter

import android.view.ViewGroup
import com.mcgrady.xproject.common.core.base.recycler.BaseViewHolder
import com.mcgrady.xproject.common.core.base.recycler.BindingListAdapter
import com.mcgrady.xproject.model.SettingMainBean
import com.mcgrady.xproject.ui.main.R
import com.mcgrady.xproject.ui.main.databinding.RecyclerItemSettingMainBinding
import com.mcgrady.xproject.util.SettingHandlers

/**
 * Created by mcgrady on 2022/2/17.
 */
class SettingMainListAdapter :
    BindingListAdapter<SettingMainBean, RecyclerItemSettingMainBinding>(SettingMainBean.CALLBACK) {

    override val layoutResId: Int
        get() = R.layout.recycler_item_setting_main

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<RecyclerItemSettingMainBinding> {
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun bind(binding: RecyclerItemSettingMainBinding, item: SettingMainBean) {
        binding.apply {
            model = item
            handler = SettingHandlers()
            executePendingBindings()
        }
    }
}