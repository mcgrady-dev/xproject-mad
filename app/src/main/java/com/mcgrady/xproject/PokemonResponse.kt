package com.mcgrady.xproject

/**
 * Created by mcgrady on 2021/8/12.
 */
data class PokemonResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)

data class Result(
    val name: String,
    val url: String
)
