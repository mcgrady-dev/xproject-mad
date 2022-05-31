package com.mcgrady.xproject.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcgrady.xproject.model.SettingMainBean
import com.mcgrady.xproject.repo.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingMainViewModel @Inject constructor(repository: SettingRepository) : ViewModel() {

    private val _list = MutableLiveData<List<SettingMainBean>>()
    val list: LiveData<List<SettingMainBean>> = _list

    init {
        _list.value = repository.fetchList()
    }
}