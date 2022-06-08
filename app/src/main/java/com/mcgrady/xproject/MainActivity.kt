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
package com.mcgrady.xproject

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mcgrady.xarch.ext.viewbind
import com.mcgrady.xproject.databinding.ActivityMainBinding
import com.tencent.vasdolly.helper.ChannelReaderUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewbind()

    @VisibleForTesting
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            tvTitle.text = "HELLO WORLD"
            tvTitle.setTextColor(Color.BLACK)
            tvTitle.setOnClickListener {
                val dm = DisplayMetrics()
                @Suppress("DEPRECATION")
                windowManager.defaultDisplay.getMetrics(dm)
                val width = dm.widthPixels.coerceAtMost(dm.heightPixels)

                Timber.d("dpi=${dm.densityDpi} smallestWidthPixels=$width")

                Timber.d("smallestWidth=${width / (dm.densityDpi / 160.0)}dp")
//                LogUtils.d("实际使用的smallestWidth :  " + resources.getString(R.string.base_dpi))
            }

            tvChannel.text = ChannelReaderUtil.getChannel(applicationContext) ?: "null"
        }

        viewModel.currentName.observe(this) {
            binding.tvTitle.text = it
        }

        viewModel.currentName.value = "XXXXX"

//        viewModel.taskUpdate.observe(this, {
//            Snackbar.make(binding.root, "HELLO WORLD", Snackbar.LENGTH_LONG).show()
//        })

//        viewModel.taskUpdate.call()

        NetworkLiveData.getInstance(this).observeForever {
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
        }
    }
}
