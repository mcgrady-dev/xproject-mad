package com.mcgrady.xproject.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mcgrady.xproject.common.core.base.BaseViewModel
import com.mcgrady.xproject.data.entity.MainItemEntity
import com.mcgrady.xproject.repo.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : BaseViewModel() {

    private val _mainItemEntitiesLiveData = MutableLiveData<List<MainItemEntity>>()
    val mainItemEntitiesLiveData: LiveData<List<MainItemEntity>> = _mainItemEntitiesLiveData

    fun initMainItemEntities() {
        _mainItemEntitiesLiveData.value = repository.fetchMainItemEntities()
    }
}