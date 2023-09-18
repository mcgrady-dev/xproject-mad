package com.mcgrady.xproject.data.mediaplayer.model.subsonic.index

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist.Artist
import com.squareup.moshi.Json

data class Index(
    @Json(name = "artist")
    val artists: List<Artist>,
    val name: String
)