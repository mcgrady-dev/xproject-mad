package com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Artist(
    val id: String,
    val name: String,
    val starred: Date? = null,
    val userRating: Int = 0,
    val averageRating: Double
) : Parcelable