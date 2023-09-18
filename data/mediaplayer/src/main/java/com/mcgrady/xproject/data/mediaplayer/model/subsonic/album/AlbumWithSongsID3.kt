package com.mcgrady.xproject.data.mediaplayer.model.subsonic.album

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class AlbumWithSongsID3 : AlbumID3() {
    @Json(name = "song")
    val songs: List<Child> = emptyList()
}