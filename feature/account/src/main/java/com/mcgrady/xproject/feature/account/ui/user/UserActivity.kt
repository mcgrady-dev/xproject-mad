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
package com.mcgrady.xproject.feature.account.ui.user

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.mcgrady.xarch.extension.viewBinding
import com.mcgrady.xproject.feature.account.databinding.ActivityUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityUserBinding::inflate)
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.userLiveData.observe(this) {
            LogUtils.dTag("User", it.toString())
        }

        viewModel.userListLiveData.observe(this) {
            LogUtils.dTag("User", "list.size = ${it?.size}")
            it?.forEach { user ->
                LogUtils.dTag("User", user.toString())
            }
        }

        binding.apply {
//            viewModel.fetchUser(Random.nextInt())
//            viewModel.fetchUsers()

            viewModel.fetchUsersSuspend()
        }
    }
}
