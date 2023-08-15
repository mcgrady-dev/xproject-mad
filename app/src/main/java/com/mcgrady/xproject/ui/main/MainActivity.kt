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

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.mcgrady.xarch.extension.viewBinding
import com.mcgrady.xproject.core.base.BaseActivity
import com.mcgrady.xproject.data.entity.MainItemEntity
import com.mcgrady.xproject.databinding.ActivityMainBinding
import com.mcgrady.xproject.feature.pokemon.databinding.ui.main.PokemonListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val viewModel: MainViewModel by viewModels()

    private val adapter by lazy { MainItemAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        binding.recyclerView.adapter = this@MainActivity.adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) ?: return@setOnItemClickListener
            when (item.id) {
                MainItemEntity.ITEM_POKEMON -> {
                    // startActivity(Intent(this@MainActivity, CustomViewActivity::class.java))
                    startActivity(Intent(this@MainActivity, PokemonListActivity::class.java))
                }
                MainItemEntity.ITEM_ZHIHU -> {
                    // startActivity(Intent(this@MainActivity, CustomViewActivity::class.java))
                }
                MainItemEntity.ITEM_MUSIC -> {
                    // startActivity(Intent(this@MainActivity, WindowInsetsControllerActivity::class.java))
                }
                MainItemEntity.ITEM_VIDEO -> {
                    // startActivity(Intent(this@MainActivity, ShapeActivity::class.java))
                }
                MainItemEntity.ITEM_CHAT -> {
                    // startActivity(Intent(this@MainActivity, SampleServiceActivity::class.java))
                }
                else -> {}
            }
        }

        viewModel.mainItemEntitiesLiveData.observe(this) {
            adapter.submitList(it)
        }
        viewModel.initMainItemEntities()
    }
}
