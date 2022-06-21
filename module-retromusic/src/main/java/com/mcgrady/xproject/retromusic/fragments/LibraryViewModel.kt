/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject.retromusic.fragments

import android.animation.ValueAnimator
import android.content.Context
import androidx.core.animation.doOnEnd
import androidx.lifecycle.*
import com.mcgrady.xproject.retromusic.*
import com.mcgrady.xproject.retromusic.db.*
import com.mcgrady.xproject.retromusic.extensions.showToast
import com.mcgrady.xproject.retromusic.fragments.ReloadType.*
import com.mcgrady.xproject.retromusic.fragments.search.Filter
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import com.mcgrady.xproject.retromusic.interfaces.IMusicServiceEventListener
import com.mcgrady.xproject.retromusic.model.*
import com.mcgrady.xproject.retromusic.repository.RealRepository
import com.mcgrady.xproject.retromusic.util.DensityUtil
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.retromusic.util.logD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class LibraryViewModel(
    private val repository: RealRepository,
) : ViewModel(), IMusicServiceEventListener {

    private val _paletteColor = MutableLiveData<Int>()
    private val home = MutableLiveData<List<Home>>()
    private val suggestions = MutableLiveData<List<Song>>()
    private val albums = MutableLiveData<List<Album>>()
    private val songs = MutableLiveData<List<Song>>()
    private val artists = MutableLiveData<List<Artist>>()
    private val playlists = MutableLiveData<List<PlaylistWithSongs>>()
    private val genres = MutableLiveData<List<Genre>>()
    private val searchResults = MutableLiveData<List<Any>>()
    private val fabMargin = MutableLiveData(0)
    private val songHistory = MutableLiveData<List<Song>>()
    private var previousSongHistory = ArrayList<HistoryEntity>()
    val paletteColor: LiveData<Int> = _paletteColor

    init {
        loadLibraryContent()
    }

    private fun loadLibraryContent() = viewModelScope.launch(IO) {
        fetchHomeSections()
        fetchSuggestions()
        fetchSongs()
        fetchAlbums()
        fetchArtists()
        fetchGenres()
        fetchPlaylists()
    }

    fun getSearchResult(): LiveData<List<Any>> = searchResults

    fun getSongs(): LiveData<List<Song>> = songs

    fun getAlbums(): LiveData<List<Album>> = albums

    fun getArtists(): LiveData<List<Artist>> = artists

    fun getPlaylists(): LiveData<List<PlaylistWithSongs>> = playlists

    fun getGenre(): LiveData<List<Genre>> = genres

    fun getHome(): LiveData<List<Home>> = home

    fun getSuggestions(): LiveData<List<Song>> = suggestions

    fun getFabMargin(): LiveData<Int> = fabMargin

    private suspend fun fetchSongs() {
        songs.postValue(repository.allSongs())
    }

    private suspend fun fetchAlbums() {
        albums.postValue(repository.fetchAlbums())
    }

    private suspend fun fetchArtists() {
        if (PreferenceUtil.albumArtistsOnly) {
            artists.postValue(repository.albumArtists())
        } else {
            artists.postValue(repository.fetchArtists())
        }
    }

    private suspend fun fetchPlaylists() {
        playlists.postValue(repository.fetchPlaylistWithSongs())
    }

    private suspend fun fetchGenres() {
        genres.postValue(repository.fetchGenres())
    }

    private suspend fun fetchHomeSections() {
        home.postValue(repository.homeSections())
    }

    private suspend fun fetchSuggestions() {
        suggestions.postValue(repository.suggestions())
    }

    fun search(query: String?, filter: Filter) =
        viewModelScope.launch(IO) {
            val result = repository.search(query, filter)
            searchResults.postValue(result)
        }

    fun forceReload(reloadType: ReloadType) = viewModelScope.launch(IO) {
        when (reloadType) {
            Songs -> fetchSongs()
            Albums -> fetchAlbums()
            Artists -> fetchArtists()
            HomeSections -> fetchHomeSections()
            Playlists -> fetchPlaylists()
            Genres -> fetchGenres()
            Suggestions -> fetchSuggestions()
        }
    }

    fun updateColor(newColor: Int) {
        _paletteColor.postValue(newColor)
    }

    override fun onMediaStoreChanged() {
        logD("onMediaStoreChanged")
        loadLibraryContent()
    }

    override fun onServiceConnected() {
        logD("onServiceConnected")
    }

    override fun onServiceDisconnected() {
        logD("onServiceDisconnected")
    }

    override fun onQueueChanged() {
        logD("onQueueChanged")
    }

    override fun onPlayingMetaChanged() {
        logD("onPlayingMetaChanged")
    }

    override fun onPlayStateChanged() {
        logD("onPlayStateChanged")
    }

    override fun onRepeatModeChanged() {
        logD("onRepeatModeChanged")
    }

    override fun onShuffleModeChanged() {
        logD("onShuffleModeChanged")
    }

    override fun onFavoriteStateChanged() {
        logD("onFavoriteStateChanged")
    }

    fun shuffleSongs() = viewModelScope.launch(IO) {
        val songs = repository.allSongs()
        withContext(Main) {
            MusicPlayerRemote.openAndShuffleQueue(songs, true)
        }
    }

    fun renameRoomPlaylist(playListId: Long, name: String) = viewModelScope.launch(IO) {
        repository.renameRoomPlaylist(playListId, name)
    }

    fun deleteSongsInPlaylist(songs: List<SongEntity>) {
        viewModelScope.launch(IO) {
            repository.deleteSongsInPlaylist(songs)
            forceReload(Playlists)
        }
    }

    fun deleteSongsFromPlaylist(playlists: List<PlaylistEntity>) = viewModelScope.launch(IO) {
        repository.deletePlaylistSongs(playlists)
    }

    fun deleteRoomPlaylist(playlists: List<PlaylistEntity>) = viewModelScope.launch(IO) {
        repository.deleteRoomPlaylist(playlists)
    }

    fun albumById(id: Long) = repository.albumById(id)
    suspend fun artistById(id: Long) = repository.artistById(id)
    suspend fun favoritePlaylist() = repository.favoritePlaylist()
    suspend fun isFavoriteSong(song: SongEntity) = repository.isFavoriteSong(song)
    suspend fun isSongFavorite(songId: Long) = repository.isSongFavorite(songId)
    suspend fun insertSongs(songs: List<SongEntity>) = repository.insertSongs(songs)
    suspend fun removeSongFromPlaylist(songEntity: SongEntity) =
        repository.removeSongFromPlaylist(songEntity)

    private suspend fun checkPlaylistExists(playlistName: String): List<PlaylistEntity> =
        repository.checkPlaylistExists(playlistName)

    private suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long =
        repository.createPlaylist(playlistEntity)

    fun importPlaylists() = viewModelScope.launch(IO) {
        val playlists = repository.fetchLegacyPlaylist()
        playlists.forEach { playlist ->
            val playlistEntity = repository.checkPlaylistExists(playlist.name).firstOrNull()
            if (playlistEntity != null) {
                val songEntities = playlist.getSongs().map {
                    it.toSongEntity(playlistEntity.playListId)
                }
                repository.insertSongs(songEntities)
            } else {
                if (playlist != Playlist.empty) {
                    val playListId = createPlaylist(PlaylistEntity(playlistName = playlist.name))
                    val songEntities = playlist.getSongs().map {
                        it.toSongEntity(playListId)
                    }
                    repository.insertSongs(songEntities)
                }
            }
            forceReload(Playlists)
        }
    }

    fun recentSongs(): LiveData<List<Song>> = liveData(IO) {
        emit(repository.recentSongs())
    }

    fun playCountSongs(): LiveData<List<Song>> = liveData(IO) {
        repository.playCountSongs().forEach { song ->
            if (!File(song.data).exists() || song.id == -1L) {
                repository.deleteSongInPlayCount(song)
            }
        }
        emit(
            repository.playCountSongs().map {
                it.toSong()
            }
        )
    }

    fun artists(type: Int): LiveData<List<Artist>> = liveData(IO) {
        when (type) {
            TOP_ARTISTS -> emit(repository.topArtists())
            RECENT_ARTISTS -> {
                emit(repository.recentArtists())
            }
        }
    }

    fun albums(type: Int): LiveData<List<Album>> = liveData(IO) {
        when (type) {
            TOP_ALBUMS -> emit(repository.topAlbums())
            RECENT_ALBUMS -> {
                emit(repository.recentAlbums())
            }
        }
    }

    fun artist(artistId: Long): LiveData<Artist> = liveData(IO) {
        emit(repository.artistById(artistId))
    }

    fun fetchContributors(): LiveData<List<Contributor>> = liveData(IO) {
        emit(repository.contributor())
    }

    fun observableHistorySongs(): LiveData<List<Song>> {
        viewModelScope.launch(IO) {
            repository.historySong().forEach { song ->
                if (!File(song.data).exists() || song.id == -1L) {
                    repository.deleteSongInHistory(song.id)
                }
            }

            songHistory.postValue(
                repository.historySong().map {
                    it.toSong()
                }
            )
        }
        return songHistory
    }

    fun clearHistory() {
        viewModelScope.launch(IO) {
            previousSongHistory = repository.historySong() as ArrayList<HistoryEntity>

            repository.clearSongHistory()
        }
        songHistory.value = emptyList()
    }

    fun restoreHistory() {
        viewModelScope.launch(IO) {
            if (previousSongHistory.isNotEmpty()) {
                val history = ArrayList<Song>()
                for (song in previousSongHistory) {
                    repository.addSongToHistory(song.toSong())
                    history.add(song.toSong())
                }
                songHistory.postValue(history)
            }
        }
    }

    fun favorites() = repository.favorites()

    fun clearSearchResult() {
        searchResults.value = emptyList()
    }

    fun addToPlaylist(context: Context, playlistName: String, songs: List<Song>) {
        viewModelScope.launch(IO) {
            val playlists = checkPlaylistExists(playlistName)
            if (playlists.isEmpty()) {
                val playlistId: Long =
                    createPlaylist(PlaylistEntity(playlistName = playlistName))
                insertSongs(songs.map { it.toSongEntity(playlistId) })
                withContext(Main) {
                    context.showToast(
                        context.getString(
                            R.string.playlist_created_sucessfully,
                            playlistName
                        )
                    )
                }
            } else {
                val playlist = playlists.firstOrNull()
                if (playlist != null) {
                    insertSongs(
                        songs.map {
                            it.toSongEntity(playListId = playlist.playListId)
                        }
                    )
                }
            }
            forceReload(Playlists)
            withContext(Main) {
                context.showToast(
                    context.getString(
                        R.string.added_song_count_to_playlist,
                        songs.size,
                        playlistName
                    )
                )
            }
        }
    }

    fun setFabMargin(context: Context, bottomMargin: Int) {
        val currentValue = DensityUtil.dip2px(context, 16F) +
            bottomMargin
        ValueAnimator.ofInt(fabMargin.value!!, currentValue).apply {
            addUpdateListener {
                fabMargin.postValue(
                    (it.animatedValue as Int)
                )
            }
            doOnEnd {
                fabMargin.postValue(currentValue)
            }
            start()
        }
    }
}

enum class ReloadType {
    Songs,
    Albums,
    Artists,
    HomeSections,
    Playlists,
    Genres,
    Suggestions
}
