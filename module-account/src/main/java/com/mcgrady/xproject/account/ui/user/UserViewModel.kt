package com.mcgrady.xproject.account.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.mcgrady.xproject.account.data.model.BaseResponse
import com.mcgrady.xproject.account.data.model.User
import com.mcgrady.xproject.account.repo.AccountRepository
import com.mcgrady.xproject.common.core.base.BaseViewModel
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: AccountRepository
) : BaseViewModel() {

    private val _userLiveData: MutableLiveData<User> = MutableLiveData()
    val userLiveData: LiveData<User> get() = _userLiveData

    private val _userListLiveData: MutableLiveData<List<User>?> = MutableLiveData()
    val userListLiveData: LiveData<List<User>?> get() = _userListLiveData

    private val _userListFlow = MutableStateFlow<List<User>?>(emptyList())
    val userListFlow: StateFlow<List<User>?> = _userListFlow

    fun fetchUser(userId: Int) {
        repository.fetchUserById(userId).enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                LogUtils.dTag("User", "response=${response}")
                val baseResponse = response.body()
                baseResponse?.let { user ->
                    _userLiveData.value = user
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun fetchUsers() {
        repository.fetchUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                LogUtils.dTag("User", "response=${response}")
                val list = response.body()
                list?.let {
                    _userListLiveData.value = it
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun fetchUsersSuspend() {
        viewModelScope.launch {
            repository.fetchUsersSuspend()
                .suspendOnSuccess {
//                    _userListFlow.emit(data)
                    _userListLiveData.value = data
                }.onError {
                    _toast.postValue(message())
                }
                .onException {
                    _toast.postValue(message ?: message())
                }
        }
    }

    fun fetchUserById(userId: Int) {
        viewModelScope.launch {
            repository.fetchUserByIdSuspend(userId)
                .suspendOnSuccess {

                }.onError {

                }.onException {

                }
        }
    }

}