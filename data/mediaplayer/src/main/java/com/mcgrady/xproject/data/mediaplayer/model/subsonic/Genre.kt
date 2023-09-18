package com.mcgrady.xproject.data.mediaplayer.model.subsonic

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    @Json(name = "value")
    val genre: String,
    val songCount: Int,
    val albumCount: Int
) : Parcelable