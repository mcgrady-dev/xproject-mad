package com.mcgrady.xproject.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.mcgrady.xarch.extension.viewBinding
import com.mcgrady.xproject.common.core.base.BaseActivity
import com.mcgrady.xproject.data.entity.MainItemEntity
import com.mcgrady.xproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val viewModel: MainViewModel by viewModels()

    private val adapter by lazy { MainItemAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding.apply {
            recyclerView.adapter = this@MainActivity.adapter
            adapter.setOnItemClickListener { adapter, view, position ->
                val item = adapter.getItem(position) ?: return@setOnItemClickListener
                when (item.id) {
                    MainItemEntity.ITEM_POKEMON -> {
                        //startActivity(Intent(this@MainActivity, CustomViewActivity::class.java))
                    }
                    MainItemEntity.ITEM_ZHIHU -> {
                        //startActivity(Intent(this@MainActivity, CustomViewActivity::class.java))
                    }
                    MainItemEntity.ITEM_MUSIC -> {
                        //startActivity(Intent(this@MainActivity, WindowInsetsControllerActivity::class.java))
                    }
                    MainItemEntity.ITEM_VIDEO -> {
                        //startActivity(Intent(this@MainActivity, ShapeActivity::class.java))
                    }
                    MainItemEntity.ITEM_CHAT -> {
                        //startActivity(Intent(this@MainActivity, SampleServiceActivity::class.java))
                    }
                    else -> {}
                }
            }
        }

        viewModel.mainItemEntitiesLiveData.observe(this) {
            adapter.submitList(it)
        }
        viewModel.initMainItemEntities()

    }
}