package com.mcgrady.xproject.data.mediaplayer.model.subsonic

import android.app.appsearch.SearchResult
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.album.AlbumInfo
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.album.AlbumList
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.album.AlbumList2
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.album.AlbumWithSongsID3
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist.ArtistInfo
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist.ArtistInfo2
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist.ArtistWithAlbumsID3
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.artist.ArtistsID3
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.bookmark.Bookmarks
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.folder.MusicFolders
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.index.Indexes
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.playlist.PlayQueue
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.playlist.PlaylistWithSongs
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.playlist.Playlists
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.search.SearchResult2
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.search.SearchResult3
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.share.Shares
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.similar.SimilarSongs
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.similar.SimilarSongs2
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.user.User
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.user.Users
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.video.VideoInfo
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.video.Videos

data class SubsonicModel(
    val error: Error? = null,
    val scanStatus: ScanStatus? = null,
    val topSongs: TopSongs? = null,
    val similarSongs2: SimilarSongs2? = null,
    val similarSongs: SimilarSongs? = null,
    val artistInfo2: ArtistInfo2? = null,
    val artistInfo: ArtistInfo? = null,
    val albumInfo: AlbumInfo? = null,
    val starred2: Starred2? = null,
    val starred: Starred? = null,
    val shares: Shares? = null,
    val playQueue: PlayQueue? = null,
    val bookmarks: Bookmarks? = null,
    val internetRadioStations: InternetRadioStations? = null,
//    val newestPodcasts: NewestPodcasts? = null,
//    val podcasts: Podcasts? = null,
    val lyrics: Lyrics? = null,
    val songsByGenre: Songs? = null,
    val randomSongs: Songs? = null,
    val albumList2: AlbumList2? = null,
    val albumList: AlbumList? = null,
    val chatMessages: ChatMessages? = null,
    val user: User? = null,
    val users: Users? = null,
    val license: License? = null,
    val jukeboxPlaylist: JukeboxPlaylist? = null,
    val jukeboxStatus: JukeboxStatus? = null,
    val playlist: PlaylistWithSongs? = null,
    val playlists: Playlists? = null,
    val searchResult3: SearchResult3? = null,
    val searchResult2: SearchResult2? = null,
    val searchResult: SearchResult? = null,
    val nowPlaying: NowPlaying? = null,
    val videoInfo: VideoInfo? = null,
    val videos: Videos? = null,
    val song: Child? = null,
    val album: AlbumWithSongsID3? = null,
    val artist: ArtistWithAlbumsID3? = null,
    val artists: ArtistsID3? = null,
    val genres: Genres? = null,
    val directory: Directory? = null,
    val indexes: Indexes? = null,
    val musicFolders: MusicFolders? = null,
    val status: String? = null,
    val version: String? = null,
    val type: String? = null,
    val serverVersion: String? = null,
)