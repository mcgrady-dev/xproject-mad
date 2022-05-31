package com.mcgrady.xproject.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.mcgrady.xproject.common.core.base.BaseFragment
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.ui.main.R
import com.mcgrady.xproject.ui.main.databinding.FragmentSettingSecondaryPageBinding


class SettingAccountFragment : BaseFragment() {

    private val binding: FragmentSettingSecondaryPageBinding by viewbind()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            message.text = "Hello Navigation"
            context?.let { message.setTextColor(ContextCompat.getColor(it, R.color.white)) }
        }
    }

    override fun initData() {

    }
}