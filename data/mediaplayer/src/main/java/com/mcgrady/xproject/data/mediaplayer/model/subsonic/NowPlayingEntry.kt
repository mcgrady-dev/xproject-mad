package com.mcgrady.xproject.data.mediaplayer.model.subsonic

import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class NowPlayingEntry(
    @Json(name = "_id")
    override val id: String
) : Child(id) {
    var username: String? = null
    var minutesAgo: Int = 0
    var playerId: Int = 0
    var playerName: String? = null
}