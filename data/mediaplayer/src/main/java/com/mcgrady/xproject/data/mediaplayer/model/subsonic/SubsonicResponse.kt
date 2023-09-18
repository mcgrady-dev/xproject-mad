package com.mcgrady.xproject.data.mediaplayer.model.subsonic

import com.squareup.moshi.Json

data class SubsonicResponse(
    @Json(name = "subsonic-response")
    val subsonicResponse: SubsonicModel
)