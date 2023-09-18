package com.mcgrady.xproject.data.mediaplayer.utils

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi


@UnstableApi
object MediaMappingUtils {
}

fun List<com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child>.toMediaItems() = this.map { it.toMediaItem() }

fun com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child.toMediaItem(): MediaItem {
//    val uri: Uri = getUri(media)
    val uri: Uri = Uri.parse("")
    val bundle = this.toBundle().apply {
        putString("uri", uri.toString())
    }
    return MediaItem.Builder()
        .setMediaId(id)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(MusicUtils.getReadableString(title))
                .setTrackNumber(track ?: 0)
                .setDiscNumber(discNumber ?: 0)
                .setReleaseYear(year ?: 0)
                .setAlbumTitle(MusicUtils.getReadableString(album))
                .setArtist(MusicUtils.getReadableString(artist))
                .setExtras(bundle)
                .build()
        )
        .setRequestMetadata(
            MediaItem.RequestMetadata.Builder()
                .setMediaUri(uri)
                .setExtras(bundle)
                .build()
        )
        .setMimeType(MimeTypes.BASE_TYPE_AUDIO)
        .setUri(uri)
        .build()
}

inline fun com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child.toBundle() = Bundle().apply {
    putString("id", id)
    putString("parentId", parentId)
    putBoolean("isDir", isDir)
    putString("title", title)
    putString("album", album)
    putString("artist", artist)
    putInt("track", track ?: 0)
    putInt("year", year ?: 0)
    putString("genre", genre)
    putString("coverArtId", coverArtId)
    putLong("size", size ?: 0L)
    putString("contentType", contentType)
    putString("suffix", suffix)
    putString("transcodedContentType", transcodedContentType)
    putString("transcodedSuffix", transcodedSuffix)
    putInt("duration", duration ?: 0)
    putInt("bitrate", bitrate ?: 0)
    putString("path", path)
    putBoolean("isVideo", isVideo)
    putInt("userRating", userRating ?: 0)
    putDouble("averageRating", (averageRating ?: 0F) as Double)
    putLong("playCount", playCount ?: 0L)
    putInt("discNumber", discNumber ?: 0)
    putLong("created", created?.time ?: 0L)
    putLong("starred", starred?.time ?: 0)
    putString("albumId", albumId)
    putString("artistId", artistId)
    putString("type", type)
    putLong("bookmarkPosition", bookmarkPosition ?: 0L)
    putInt("originalWidth", originalWidth ?: 0)
    putInt("originalHeight", originalHeight ?: 0)
}