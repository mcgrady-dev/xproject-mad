package com.mcgrady.xproject.repo

import android.content.Context
import com.mcgrady.xproject.common.core.repo.Repository
import com.mcgrady.xproject.model.SettingMainBean
import com.mcgrady.xproject.ui.main.R

/**
 * Created by mcgrady on 2022/5/31.
 */
class SettingRepository constructor(private val context: Context): Repository {

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
        ),
    )
}