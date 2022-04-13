package com.mcgrady.xproject.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mcgrady.xarchitecture.base.BaseFragment
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.ui.main.databinding.FragmentSettingSecondaryPageBinding

class SettingAppInfoFragment : BaseFragment() {

    private val binding: FragmentSettingSecondaryPageBinding by viewbind()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun initData() {

    }
}