package com.mcgrady.xproject.pokemon.repository

import com.mcgrady.xarchitecture.repository.Repository
import com.mcgrady.xproject.pokemon.network.PokedexClient
import javax.inject.Inject

/**
 * Created by mcgrady on 2021/10/15.
 */
class PokedexRepository @Inject constructor(
    private val pokedexClient: PokedexClient
) : Repository {

//    @WorkerThread
//    fun fetchPokemonList(
//        page: Int,
//        onStart: () -> Unit,
//        onComplete: () -> Unit,
//        onError: (String?) -> Unit
//    ) = flow {
//        /**
//         * fetches a list of [Pokemon] from the network and getting [ApiResponse] asynchronously.
//         * @see [suspendOnSuccess](https://github.com/skydoves/sandwich#suspendonsuccess-suspendonerror-suspendonexception)
//         */
//        /**
//         * fetches a list of [Pokemon] from the network and getting [ApiResponse] asynchronously.
//         * @see [suspendOnSuccess](https://github.com/skydoves/sandwich#suspendonsuccess-suspendonerror-suspendonexception)
//         */
//        val response = pokedexClient.fetchPokemonList(page = page)
//        response.suspendOnSuccess {
//            data.whatIfNotNull { response ->
//                pokemons = response.results
//                pokemons.forEach { pokemon -> pokemon.page = page }
//                pokemonDao.insertPokemonList(pokemons)
//                emit(pokemonDao.getAllPokemonList(page))
//            }
//        }
//            // handles the case when the API request gets an error response.
//            // e.g., internal server error.
//            .onError {
//                /** maps the [ApiResponse.Failure.Error] to the [PokemonErrorResponse] using the mapper. */
//                /** maps the [ApiResponse.Failure.Error] to the [PokemonErrorResponse] using the mapper. */
//                map(ErrorResponseMapper) { onError("[Code: $code]: $message") }
//            }
//            // handles the case when the API request gets an exception response.
//            // e.g., network connection error.
//            .onException { onError(message) }
//    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)
}