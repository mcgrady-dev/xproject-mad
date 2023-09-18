package com.mcgrady.xproject.data.mediaplayer.model.subsonic.playlist

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "playlist")
open class Playlist(
    @PrimaryKey
    @ColumnInfo(name = "id")
    open var id: String
) : Parcelable {
    @ColumnInfo(name = "name")
    var name: String? = null
    @Ignore
    var comment: String? = null
    @Ignore
    var owner: String? = null
    @Ignore
    @Json(name = "public")
    var isUniversal: Boolean? = null
    @Ignore
    var songCount: Int = 0
    @ColumnInfo(name = "duration")
    var duration: Long = 0
    @Ignore
    var created: Date? = null
    @Ignore
    var changed: Date? = null
    @ColumnInfo(name = "coverArt")
    var coverArtId: String? = null
    @Ignore
    var allowedUsers: List<String>? = null
}