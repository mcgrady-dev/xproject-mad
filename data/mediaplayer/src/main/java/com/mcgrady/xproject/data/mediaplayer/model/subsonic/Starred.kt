package com.mcgrady.xproject.data.mediaplayer.model.subsonic

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist.Artist


data class Starred(
    val artists: List<Artist>,
    val albums: List<Child>,
    val songs: List<Child>
)