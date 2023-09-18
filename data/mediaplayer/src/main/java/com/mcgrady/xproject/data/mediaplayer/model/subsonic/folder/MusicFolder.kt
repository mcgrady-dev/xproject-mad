package com.mcgrady.xproject.data.mediaplayer.model.subsonic.folder

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicFolder(
    val id: String,
    val name: String
) : Parcelable


