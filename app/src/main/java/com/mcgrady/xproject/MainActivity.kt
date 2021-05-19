package com.mcgrady.xproject

import android.os.Bundle
import androidx.activity.viewModels
import com.mcgrady.xarchitecture.BaseActivity
import com.mcgrady.xproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    val viewModel by viewModels<MainViewModel>()

    override fun observeViewModel() {
    }

    override fun initView(savedInstanceState: Bundle?) {


    }

}