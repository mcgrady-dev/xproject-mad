package com.mcgrady.xproject.data.mediaplayer.model.subsonic

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.album.AlbumID3
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist.ArtistID3
import com.squareup.moshi.Json

data class Starred2(
    @Json(name = "artist")
    val artists: List<ArtistID3>,
    @Json(name = "album")
    val albums: List<AlbumID3>,
    @Json(name = "song")
    val songs: List<Child>,
)