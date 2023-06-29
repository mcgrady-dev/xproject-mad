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
package com.mcgrady.xproject.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mcgrady.xproject.core.base.BaseViewModel
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
