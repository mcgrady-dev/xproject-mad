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
package com.mcgrady.xproject.main.repo

import android.content.Context
import com.mcgrady.xproject.common.core.repo.Repository
import com.mcgrady.xproject.main.R
import com.mcgrady.xproject.main.model.SettingMainBean

/**
 * Created by mcgrady on 2022/5/31.
 */
class SettingRepository constructor(private val context: Context) : Repository {

    fun fetchList() = mutableListOf(
        SettingMainBean(
            R.id.action_SettingMainFragment_to_SettingAccountFragment,
            context.getString(R.string.main_account_info),
            R.drawable.ic_baseline_account_circle_24
        ),
        SettingMainBean(
            R.id.action_SettingMainFragment_to_SettingAppInfoFragment,
            context.getString(R.string.main_current_version),
            R.drawable.ic_baseline_update_24,
            "v2.0.0"
        ),
        SettingMainBean(
            R.id.action_SettingMainFragment_to_SettingDeviceInfoFragment,
            context.getString(R.string.main_device_info),
            R.drawable.ic_baseline_info_24
        ),
        SettingMainBean(
            R.id.action_SettingMainFragment_to_LogoutDialog,
            context.getString(R.string.main_logout),
            R.drawable.ic_baseline_exit_to_app_24
        )
    )
}
