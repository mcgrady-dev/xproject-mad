package com.mcgrady.xproject.data.mediaplayer.model.subsonic.playlist

import com.squareup.moshi.Json

data class Playlists(
    @Json(name = "playlist")
    val playlists: List<Playlist>
)