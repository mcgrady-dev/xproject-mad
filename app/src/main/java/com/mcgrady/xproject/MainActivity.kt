package com.mcgrady.xproject

import android.os.Bundle
import androidx.activity.viewModels
import com.mcgrady.xarchitecture.base.BaseActivity
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    val binding: ActivityMainBinding by viewbind()

    val viewModel by viewModels<MainViewModel>()

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

}