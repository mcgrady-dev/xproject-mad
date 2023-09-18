package com.mcgrady.xproject.data.mediaplayer.model.subsonic.user

import java.util.Date


data class User(
    val folders: List<Int>,
    val username: String,
    val email: String,
    val isScrobblingEnabled: Boolean,
    val maxBitRate: Int,
    val isAdminRole: Boolean,
    val isSettingsRole: Boolean,
    val isDownloadRole: Boolean,
    val isUploadRole: Boolean,
    val isPlaylistRole: Boolean,
    val isCoverArtRole: Boolean,
    val isCommentRole: Boolean,
    val isPodcastRole: Boolean,
    val isStreamRole: Boolean,
    val isJukeboxRole: Boolean,
    val isShareRole: Boolean,
    val isVideoConversionRole: Boolean,
    val avatarLastChanged: Date? = null
)