package com.mcgrady.xproject.data.mediaplayer.model.subsonic.album

import com.squareup.moshi.Json


data class AlbumList2(@Json(name = "album") var albums: List<AlbumID3>)