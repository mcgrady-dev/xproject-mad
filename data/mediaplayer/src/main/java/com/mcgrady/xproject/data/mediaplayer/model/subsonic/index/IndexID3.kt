package com.mcgrady.xproject.data.mediaplayer.model.subsonic.index

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist.ArtistID3
import com.squareup.moshi.Json

data class IndexID3(
    @Json(name = "artist")
    val artists: List<ArtistID3>,
    val name: String
)