package com.mcgrady.xproject.data.mediaplayer.model.subsonic

import com.squareup.moshi.Json

data class InternetRadioStations(
    @Json(name = "internetRadioStation")
    val internetRadioStations: List<InternetRadioStation>
)