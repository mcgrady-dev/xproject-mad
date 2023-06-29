package com.mcgrady.xproject.feature.account.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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