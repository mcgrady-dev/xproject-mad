package com.mcgrady.xproject.data.mediaplayer.model.subsonic

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
class Directory : Parcelable {
    @Json(name = "child")
    var children: List<Child>? = null
    var id: String? = null
    @Json(name = "parent")
    var parentId: String? = null
    var name: String? = null
    var starred: Date? = null
    var userRating: Int? = null
    var averageRating: Double? = null
    var playCount: Long? = null
}