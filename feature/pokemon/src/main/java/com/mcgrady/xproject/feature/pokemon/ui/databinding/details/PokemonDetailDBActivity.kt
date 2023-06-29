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
package com.mcgrady.xproject.feature.pokemon.ui.databinding.details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.mcgrady.xarch.extension.viewBinding
import com.mcgrady.xproject.core.base.BaseActivity
import com.mcgrady.xproject.core.ui.transformationlayout.TransformationCompat
import com.mcgrady.xproject.core.ui.transformationlayout.TransformationLayout
import com.mcgrady.xproject.core.ui.transformationlayout.onTransformationEndContainer
import com.mcgrady.xproject.feature.pokemon.data.model.Pokemon
import com.mcgrady.xproject.feature.pokemon.databinding.PokemonActivityDetailDbBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PokemonDetailDBActivity : BaseActivity() {

    @set:Inject
    internal lateinit var viewModelFactory: PokemonDetailDBViewModel.PokemonDetailDBViewModelFactory

    private val viewModel: PokemonDetailDBViewModel by viewModels {
        PokemonDetailDBViewModel.provideFactory(viewModelFactory, pokemon.name)
    }

    private val pokemon: Pokemon by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_POKEMON, Pokemon::class.java) as Pokemon
        } else {
            intent.getParcelableExtra<Pokemon>(EXTRA_POKEMON) as Pokemon
        }
    }

    private val binding by viewBinding(PokemonActivityDetailDbBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainer(
            intent.getParcelableArrayExtra(EXTRA_TRANSITION_LAYOUT) as TransformationLayout.Params,
        )
        super.onCreate(savedInstanceState)

        binding.apply {
            pokemon = this@PokemonDetailDBActivity.pokemon
            vm = viewModel
        }
    }

    companion object {
        internal const val EXTRA_TRANSITION_LAYOUT = "EXTRA_TRANSITION_LAYOUT"
        internal const val EXTRA_POKEMON = "EXTRA_POKEMON"

        fun startActivity(transformationLayout: TransformationLayout, pokemon: Pokemon) {
            val intent = Intent(transformationLayout.context, PokemonDetailDBActivity::class.java).apply {
                putExtra(EXTRA_POKEMON, pokemon)
            }
            TransformationCompat.startActivity(transformationLayout, intent)
        }
    }
}
