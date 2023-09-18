package com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist

import android.os.Parcelable
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.album.AlbumID3
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class ArtistWithAlbumsID3 : ArtistID3(), Parcelable {
    @Json(name = "album")
    var albums: List<AlbumID3>? = null
}