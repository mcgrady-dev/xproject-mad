package com.mcgrady.xproject.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.StringUtils
import com.mcgrady.xproject.model.SettingMainModel
import com.mcgrady.xproject.ui.main.R

class SettingMainViewModel : ViewModel() {

    private val _liveData = MutableLiveData<List<SettingMainModel>>()
    val liveData: LiveData<List<SettingMainModel>> = _liveData

    init {
        _liveData.value = mutableListOf(
            SettingMainModel(
                R.id.action_SettingMainFragment_to_SettingAccountFragment,
                StringUtils.getString(R.string.main_account_info),
                R.drawable.ic_baseline_account_circle_24
            ),
            SettingMainModel(
                R.id.action_SettingMainFragment_to_SettingAppInfoFragment,
                StringUtils.getString(R.string.main_current_version),
                R.drawable.ic_baseline_update_24,
                "v2.0.0"
            ),
            SettingMainModel(
                R.id.action_SettingMainFragment_to_SettingDeviceInfoFragment,
                StringUtils.getString(R.string.main_device_info),
                R.drawable.ic_baseline_info_24
            ),
            SettingMainModel(
                R.id.action_SettingMainFragment_to_LogoutDialog,
                StringUtils.getString(R.string.main_logout),
                R.drawable.ic_baseline_exit_to_app_24
            ),
        )
    }
}