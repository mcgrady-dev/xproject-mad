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
package com.mcgrady.xproject.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * Created by mcgrady on 4/15/21.
 */
abstract class BaseViewModel : ViewModel() {

    protected val _isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }
    val isLoading: LiveData<Boolean> = _isLoading

    protected val _toast: MutableLiveData<String> by lazy { MutableLiveData() }
    val toast: LiveData<String> = _toast

    fun <T> Flow<T>.asLiveDataOnViewModelScope(): LiveData<T> {
        return asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    }
}
