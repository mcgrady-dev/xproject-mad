package com.mcgrady.xproject.data.mediaplayer.model.subsonic.playlist

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child
import java.util.*

@Keep
class PlayQueue {
    @SerializedName("entry")
    var entries: List<Child>? = null
    var current: String? = null
    var position: Long? = null
    var username: String? = null
    var changed: Date? = null
    var changedBy: String? = null
}