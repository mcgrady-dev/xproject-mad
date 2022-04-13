package com.mcgrady.xproject.pokemon.repository

import androidx.annotation.WorkerThread
import com.mcgrady.xarchitecture.repository.Repository
import com.mcgrady.xproject.pokemon.model.Pokemon
import com.mcgrady.xproject.pokemon.model.PokemonResponse
import com.mcgrady.xproject.pokemon.network.PokedexClient
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * Created by mcgrady on 2021/10/15.
 */
class PokedexRepository @Inject constructor(
    private val pokedexClient: PokedexClient
) : Repository {

    @WorkerThread
    fun fetchPokemonList(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
//        var pokemons = pokemonDao.getPokemonList(page)
        var pokemons: List<Pokemon> = listOf()
        if (pokemons.isEmpty()) {
            /**
             * fetches a list of [Pokemon] from the network and getting [ApiResponse] asynchronously.
             * @see [suspendOnSuccess](https://github.com/skydoves/sandwich#suspendonsuccess-suspendonerror-suspendonexception)
             */
            val response = pokedexClient.fetchPokemonList(page = page)
            response.suspendOnSuccess {
                data?.let { response ->
                    pokemons = response.results
                    pokemons.forEach { pokemon -> pokemon.page = page }
//                    pokemonDao.insertPokemonList(pokemons)
//                    emit(pokemonDao.getAllPokemonList(page))
                    emit(pokemons)
                }
            }
                // handles the case when the API request gets an error response.
                // e.g., internal server error.
                .onError {
                    /** maps the [ApiResponse.Failure.Error] to the [PokemonErrorResponse] using the mapper. */
//                    map(ErrorResponseMapper) { onError("[Code: $code]: $message") }
                }
                // handles the case when the API request gets an exception response.
                // e.g., network connection error.
                .onException { onError(message) }
        } else {
//            emit(pokemonDao.getAllPokemonList(page))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
}