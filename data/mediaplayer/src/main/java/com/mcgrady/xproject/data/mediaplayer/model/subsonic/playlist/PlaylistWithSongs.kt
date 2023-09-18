package com.mcgrady.xproject.data.mediaplayer.model.subsonic.playlist

import android.os.Parcelable
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistWithSongs(
    @Json(name = "_id")
    override var id: String
) : Playlist(id), Parcelable {
    @Json(name = "entry")
    var entries: List<Child>? = null
}