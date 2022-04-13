package com.mcgrady.xproject.pokemon.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import com.mcgrady.xarchitecture.base.BaseActivity
import com.mcgrady.xarchitecture.ext.databind
import com.mcgrady.xproject.pokemon.R
import com.mcgrady.xproject.pokemon.databinding.ActivityMainBinding
import com.mcgrady.xproject.pokemon.ui.adapter.PokemonAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by databind(R.layout.activity_main)
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@MainActivity
            adapter = PokemonAdapter()
            vm = viewModel
        }
    }

}