package com.mcgrady.xproject.pokemon.model

/**
 * Created by mcgrady on 2021/8/12.
 */
data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>
)
