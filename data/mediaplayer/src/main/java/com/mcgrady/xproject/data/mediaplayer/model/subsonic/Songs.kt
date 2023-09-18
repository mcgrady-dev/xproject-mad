package com.mcgrady.xproject.data.mediaplayer.model.subsonic

import com.squareup.moshi.Json

class Songs(
    @Json(name = "song")
    val songs: List<Child>
)