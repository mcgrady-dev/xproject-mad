package com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
open class ArtistID3 : Parcelable {
    val id: String? = null
    val name: String? = null
    @Json(name = "coverArt")
    val coverArtId: String? = null
    val albumCount = 0
    val starred: Date? = null
}