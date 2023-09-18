package com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.index.IndexID3
import com.squareup.moshi.Json

data class ArtistsID3(
    @Json(name = "index")
    val indices: List<IndexID3>,
    val ignoredArticles: String,
)