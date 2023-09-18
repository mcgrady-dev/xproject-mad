package com.mcgrady.xproject.data.mediaplayer.model.subsonic.folder

import com.squareup.moshi.Json

data class MusicFolders(
    @Json(name = "musicFolder")
    val musicFolders: List<MusicFolder>
)