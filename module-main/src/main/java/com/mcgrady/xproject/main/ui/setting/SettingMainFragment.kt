/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject.main.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mcgrady.xarch.ext.databind
import com.mcgrady.xproject.common.core.base.BaseFragment
import com.mcgrady.xproject.main.databinding.SettingMainFragmentBinding
import com.mcgrady.xproject.main.ui.adapter.SettingMainListAdapter
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
