package com.mcgrady.xproject.data.mediaplayer.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "download")
class Download(@PrimaryKey override val id: String) : Child(id) {
    @ColumnInfo(name = "playlist_id")
    var playlistId: String? = null

    @ColumnInfo(name = "playlist_name")
    var playlistName: String? = null

    @ColumnInfo(name = "download_state", defaultValue = "1")
    var downloadState: Int = 0

    @ColumnInfo(name = "download_uri", defaultValue = "")
    var downloadUri: String? = null

    constructor(child: Child) : this(child.id) {
        parentId = child.parentId
        isDir = child.isDir
        title = child.title
        album = child.album
        artist = child.artist
        track = child.track
        year = child.year
        genre = child.genre
        coverArtId = child.coverArtId
        size = child.size
        contentType = child.contentType
        suffix = child.suffix
        transcodedContentType = child.transcodedContentType
        transcodedSuffix = child.transcodedSuffix
        duration = child.duration
        bitrate = child.bitrate
        path = child.path
        isVideo = child.isVideo
        userRating = child.userRating
        averageRating = child.averageRating
        playCount = child.playCount
        discNumber = child.discNumber
        created = child.created
        starred = child.starred
        albumId = child.albumId
        artistId = child.artistId
        type = child.type
        bookmarkPosition = child.bookmarkPosition
        originalWidth = child.originalWidth
        originalHeight = child.originalHeight
    }
}

data class DownloadStack(
    var id: String,
    var view: String?
)