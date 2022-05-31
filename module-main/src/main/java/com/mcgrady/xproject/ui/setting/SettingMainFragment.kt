package com.mcgrady.xproject.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mcgrady.xarchitecture.ext.databind
import com.mcgrady.xproject.common.core.base.BaseFragment
import com.mcgrady.xproject.ui.adapter.SettingMainListAdapter
import com.mcgrady.xproject.ui.main.databinding.SettingMainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingMainFragment : BaseFragment() {

    private val binding: SettingMainFragmentBinding by databind()
    private val viewModel: SettingMainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.apply {
            lifecycleOwner = this@SettingMainFragment
            vm = viewModel
            adapter = SettingMainListAdapter()
        }.root
    }

    override fun initData() {
        with(binding) {

        }
    }

}