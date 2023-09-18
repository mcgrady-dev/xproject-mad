package com.mcgrady.xproject.data.mediaplayer.database.entity

import androidx.media3.common.MediaItem
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mcgrady.xproject.core.utils.PreferenceUtil
import com.mcgrady.xproject.data.mediaplayer.extensions.subsonicServerId
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "chronology")
class Chronology(@PrimaryKey override val id: String) : Child(id) {
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = System.currentTimeMillis()

    @ColumnInfo(name = "server")
    var server: String? = null

    constructor(mediaItem: MediaItem) : this(mediaItem.mediaMetadata.extras?.getString("id") ?: "") {
        mediaItem.mediaMetadata.extras?.let { extras ->
            parentId = extras.getString("parentId")
            isDir = extras.getBoolean("isDir")
            title = extras.getString("title")
            album = extras.getString("album")
            artist = extras.getString("artist")
            track = extras.getInt("track")
            year = extras.getInt("year")
            genre = extras.getString("genre")
            coverArtId = extras.getString("coverArtId")
            size = extras.getLong("size")
            contentType = extras.getString("contentType")
            suffix = extras.getString("suffix")
            transcodedContentType = extras.getString("transcodedContentType")
            transcodedSuffix = extras.getString("transcodedSuffix")
            duration = extras.getInt("duration")
            bitrate = extras.getInt("bitrate")
            path = extras.getString("path")
            isVideo = extras.getBoolean("isVideo")
            userRating = extras.getInt("userRating")
            averageRating = extras.getDouble("averageRating")
            playCount = extras.getLong("playCount")
            discNumber = extras.getInt("discNumber")
            created = Date(extras.getLong("created"))
            starred = Date(extras.getLong("starred"))
            albumId = extras.getString("albumId")
            artistId = extras.getString("artistId")
            type = extras.getString("type")
            bookmarkPosition = extras.getLong("bookmarkPosition")
            originalWidth = extras.getInt("originalWidth")
            originalHeight = extras.getInt("originalHeight")
        }
        
        server = PreferenceUtil.subsonicServerId
        timestamp = Date().time
    }
}