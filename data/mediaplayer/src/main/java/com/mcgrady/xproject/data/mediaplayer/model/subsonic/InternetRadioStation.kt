package com.mcgrady.xproject.data.mediaplayer.model.subsonic

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
data class InternetRadioStation(
    val id: String,
    val name: String,
    val streamUrl: String,
    val homePageUrl: String,
) : Parcelable