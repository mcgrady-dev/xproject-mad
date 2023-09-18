package com.mcgrady.xproject.data.mediaplayer.model.subsonic.bookmark

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child
import java.util.Date

data class Bookmark(
    val entry: Child,
    val position: Long = 0,
    val username: String,
    val comment: String,
    val created: Date? = null,
    val changed: Date? = null
)