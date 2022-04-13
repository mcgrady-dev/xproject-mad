package com.mcgrady.xproject.setting

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mcgrady.xarchitecture.base.BaseFragment
import com.mcgrady.xarchitecture.ext.databind
import com.mcgrady.xproject.main.R
import com.mcgrady.xproject.main.databinding.SettingMainFragmentBinding

class SettingMainFragment : BaseFragment(R.layout.setting_main_fragment) {

    companion object {
        fun newInstance() = SettingMainFragment()
    }

    private val binding: SettingMainFragmentBinding by databind()
    private val viewModel: SettingMainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

        }
    }

    override fun initData() {
    }

}