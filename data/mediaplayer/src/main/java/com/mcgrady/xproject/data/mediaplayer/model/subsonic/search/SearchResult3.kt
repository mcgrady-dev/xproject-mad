package com.mcgrady.xproject.data.mediaplayer.model.subsonic.search

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.album.AlbumID3
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist.ArtistID3
import com.squareup.moshi.Json

data class SearchResult3(
    @Json(name ="artist")
    val artists: List<ArtistID3>,
    @Json(name = "album")
    val albums: List<AlbumID3>,
    @Json(name = "song")
    val songs: List<Child>
)