package com.mcgrady.xproject.data.mediaplayer.model.subsonic.album

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
open class AlbumID3: Parcelable {

    var id: String? = ""
    var name: String? = ""
    var artist: String? = ""
    var artistId: String? = ""
    @Json(name = "coverArt") open var coverArtId: String? = ""
    var songCount: Int = 0
    var duration: Int = 0
    var playCount: Long = 0L
    var created: Date? = null
    var starred: Date? = null
    var year: Int = 0
    var genre: String? = ""
}