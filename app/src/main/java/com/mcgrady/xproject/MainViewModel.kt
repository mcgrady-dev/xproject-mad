package com.mcgrady.xproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by mcgrady on 2021/5/10.
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val taskUpdate: SingleLiveEvent<Unit> by lazy {
        SingleLiveEvent()
    }
}