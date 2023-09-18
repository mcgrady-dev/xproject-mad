package com.mcgrady.xproject.data.mediaplayer.model.subsonic.index

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist.Artist
import com.squareup.moshi.Json

data class Indexes(
    var shortcuts: List<Artist>,
    @Json(name = "index")
    val indices: List<Index>,
    val children: List<Child>,
    val lastModified: Long = 0,
    val ignoredArticles: String
)