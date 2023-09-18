package com.mcgrady.xproject.data.mediaplayer.model.subsonic.similar

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child
import com.squareup.moshi.Json

data class SimilarSongs2(
    @Json(name = "song")
    val songs: List<Child>
)