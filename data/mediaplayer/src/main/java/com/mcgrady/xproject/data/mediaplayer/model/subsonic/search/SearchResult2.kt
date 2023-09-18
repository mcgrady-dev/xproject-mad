package com.mcgrady.xproject.data.mediaplayer.model.subsonic.search

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist.Artist
import com.squareup.moshi.Json

data class SearchResult2(
    @Json(name = "artist")
    val artists: List<Artist>,
    @Json(name = "album")
    var albums: List<Child>,
    @Json(name = "song")
    var songs: List<Child>
)