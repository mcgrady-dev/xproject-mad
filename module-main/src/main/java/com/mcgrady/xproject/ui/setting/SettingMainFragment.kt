package com.mcgrady.xproject.ui.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mcgrady.xarchitecture.base.BaseFragment
import com.mcgrady.xarchitecture.ext.databind
import com.mcgrady.xproject.ui.adapter.SettingMainListAdapter

import com.mcgrady.xproject.ui.main.R
import com.mcgrady.xproject.ui.main.databinding.SettingMainFragmentBinding

class SettingMainFragment : BaseFragment(R.layout.setting_main_fragment) {

    private val binding: SettingMainFragmentBinding by databind()
    private val viewModel: SettingMainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            vm = viewModel
            adapter = SettingMainListAdapter()
        }
    }

    override fun initData() {
    }

}