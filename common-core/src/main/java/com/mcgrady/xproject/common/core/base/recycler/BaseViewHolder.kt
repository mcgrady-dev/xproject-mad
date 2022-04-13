package com.mcgrady.xproject.common.core.base.recycler

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by mcgrady on 2022/2/23.
 */
class BaseViewHolder<BD: ViewDataBinding>(val binding: BD) : RecyclerView.ViewHolder(binding.root) {

}