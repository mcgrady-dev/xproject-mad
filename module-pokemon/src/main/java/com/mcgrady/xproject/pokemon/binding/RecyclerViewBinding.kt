package com.mcgrady.xproject.pokemon.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.common.core.base.RecyclerViewPaginator
import com.mcgrady.xproject.pokemon.model.Pokemon
import com.mcgrady.xproject.pokemon.ui.adapter.PokemonAdapter
import com.mcgrady.xproject.pokemon.ui.main.MainViewModel

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
    fun paginationPokemonList(recycler: RecyclerView, viewModel: MainViewModel) {
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
            (recycler.adapter as PokemonAdapter)?.run {
                setPokemonList(pokemonList)
            }
        }
    }
}
