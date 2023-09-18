package com.mcgrady.xproject.data.mediaplayer.model.subsonic

import com.squareup.moshi.Json

data class Genres(
    @Json(name = "genre")
    val genres: List<Genre>
)