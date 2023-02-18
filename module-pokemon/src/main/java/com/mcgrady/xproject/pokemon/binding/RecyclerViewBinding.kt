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
package com.mcgrady.xproject.pokemon.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.common.core.base.RecyclerViewPaginator
import com.mcgrady.xproject.pokemon.data.model.Pokemon
import com.mcgrady.xproject.pokemon.ui.adapter.PokemonAdapter
import com.mcgrady.xproject.pokemon.ui.main.PokemonViewModel

/**
 * Created by mcgrady on 2022/1/7.
 */
object RecyclerViewBinding {

    @JvmStatic
    @BindingAdapter("adapter")
    fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        view.adapter = adapter.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    @JvmStatic
    @BindingAdapter("paginationPokemonList")
    fun paginationPokemonList(recycler: RecyclerView, viewModel: PokemonViewModel) {
        RecyclerViewPaginator(
            recyclerView = recycler,
            isLoading = { viewModel.isLoading.get() },
            loadMore = { viewModel.fetchPokemonList() },
            onLast = { false }
        ).run {
            threshold = 8
        }
    }

    @JvmStatic
    @BindingAdapter("submitPokemonList")
    fun bindSubmitPokemonList(recycler: RecyclerView, pokemonList: List<Pokemon>?) {
        if (!pokemonList.isNullOrEmpty()) {
            (recycler.adapter as PokemonAdapter?)?.run {
                setPokemonList(pokemonList)
            }
        }
    }
}
