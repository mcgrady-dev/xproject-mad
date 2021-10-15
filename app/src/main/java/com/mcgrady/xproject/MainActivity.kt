package com.mcgrady.xproject

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by viewbind()

    @VisibleForTesting
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            tvTitle.text = "HELLO WORLD"
            tvTitle.setTextColor(Color.BLACK)
        }

        viewModel.currentName.observe(this, {
            binding.tvTitle.text = it
        })

        viewModel.currentName.value = "XXXXX"

//        viewModel.taskUpdate.observe(this, {
//            Snackbar.make(binding.root, "HELLO WORLD", Snackbar.LENGTH_LONG).show()
//        })

//        viewModel.taskUpdate.call()

        NetworkLiveData.getInstance(this).observe(this, {
            when (it) {
                NetworkState.UNAVAILABLE -> {
                    Snackbar.make(binding.root, "网络不可用", Snackbar.LENGTH_SHORT).show()
                }
                NetworkState.NONE -> {
                    Snackbar.make(binding.root, "网络断开", Snackbar.LENGTH_SHORT).show()
                }
                NetworkState.CONNECT -> {
                    Snackbar.make(binding.root, "网络已连接", Snackbar.LENGTH_SHORT).show()
                }
                NetworkState.WIFI -> {
                    Snackbar.make(binding.root, "WIFI已连接", Snackbar.LENGTH_SHORT).show()
                }
                NetworkState.CELLULAR -> {
                    Snackbar.make(binding.root, "移动网络已连接", Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        })
    }

}