package com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist

import com.mcgrady.xproject.data.mediaplayer.model.subsonic.similar.SimilarArtistID3
import com.squareup.moshi.Json

class ArtistInfo2 : ArtistInfoBase() {
    @Json(name = "similarArtist")
    var similarArtists: List<SimilarArtistID3>? = emptyList()
}