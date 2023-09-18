package com.mcgrady.xproject.data.mediaplayer.model.subsonic.share

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child
import java.util.Date


data class Share(
    val entries: List<Child>,
    val id: String,
    val url: String,
    val description: String,
    val username: String,
    val created: Date? = null,
    val expires: Date? = null,
    val lastVisited: Date? = null,
    val visitCount: Int
)