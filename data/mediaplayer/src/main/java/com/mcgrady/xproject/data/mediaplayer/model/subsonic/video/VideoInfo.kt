package com.mcgrady.xproject.data.mediaplayer.model.subsonic.video

import android.media.AudioTrack
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.Captions

data class VideoInfo(
    val id: String,
    val captions: List<Captions>,
    val audioTracks: List<AudioTrack>,
    val conversions: List<VideoConversion>,
)