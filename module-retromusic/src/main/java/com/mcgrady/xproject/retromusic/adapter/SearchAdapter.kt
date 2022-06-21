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
package com.mcgrady.xproject.retromusic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.retromusic.*
import com.mcgrady.xproject.retromusic.adapter.base.MediaEntryViewHolder
import com.mcgrady.xproject.retromusic.db.PlaylistWithSongs
import com.mcgrady.xproject.retromusic.glide.GlideApp
import com.mcgrady.xproject.retromusic.glide.RetroGlideExtension
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import com.mcgrady.xproject.retromusic.helper.menu.SongMenuHelper
import com.mcgrady.xproject.retromusic.model.Album
import com.mcgrady.xproject.retromusic.model.Artist
import com.mcgrady.xproject.retromusic.model.Genre
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.util.MusicUtil
import com.mcgrady.xproject.theme.ThemeStore
import java.util.*

class SearchAdapter(
    private val activity: FragmentActivity,
    private var dataSet: List<Any>
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun swapDataSet(dataSet: List<Any>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (dataSet[position] is Album) return ALBUM
        if (dataSet[position] is Artist) return if ((dataSet[position] as Artist).isAlbumArtist) ALBUM_ARTIST else ARTIST
        if (dataSet[position] is Genre) return GENRE
        if (dataSet[position] is PlaylistWithSongs) return PLAYLIST
        return if (dataSet[position] is Song) SONG else HEADER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == HEADER) ViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.sub_header,
                parent,
                false
            ),
            viewType
        )
        else if (viewType == ALBUM || viewType == ARTIST || viewType == ALBUM_ARTIST)
            ViewHolder(
                LayoutInflater.from(activity).inflate(
                    R.layout.item_list_big,
                    parent,
                    false
                ),
                viewType
            )
        else
            ViewHolder(
                LayoutInflater.from(activity).inflate(R.layout.item_list, parent, false),
                viewType
            )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ALBUM -> {
                holder.imageTextContainer?.isVisible = true
                val album = dataSet[position] as Album
                holder.title?.text = album.title
                holder.text?.text = album.artistName
                GlideApp.with(activity).asDrawable().albumCoverOptions(album.safeGetFirstSong()).load(RetroGlideExtension.getSongModel(album.safeGetFirstSong()))
                    .into(holder.image!!)
            }
            ARTIST -> {
                holder.imageTextContainer?.isVisible = true
                val artist = dataSet[position] as Artist
                holder.title?.text = artist.name
                holder.text?.text = MusicUtil.getArtistInfoString(activity, artist)
                GlideApp.with(activity).asDrawable().artistImageOptions(artist).load(
                    RetroGlideExtension.getArtistModel(artist)
                ).into(holder.image!!)
            }
            SONG -> {
                holder.imageTextContainer?.isVisible = true
                val song = dataSet[position] as Song
                holder.title?.text = song.title
                holder.text?.text = song.albumName
                GlideApp.with(activity).asDrawable().songCoverOptions(song).load(RetroGlideExtension.getSongModel(song)).into(holder.image!!)
            }
            GENRE -> {
                val genre = dataSet[position] as Genre
                holder.title?.text = genre.name
                holder.text?.text = String.format(
                    Locale.getDefault(),
                    "%d %s",
                    genre.songCount,
                    if (genre.songCount > 1) activity.getString(R.string.songs) else activity.getString(
                        R.string.song
                    )
                )
            }
            PLAYLIST -> {
                val playlist = dataSet[position] as PlaylistWithSongs
                holder.title?.text = playlist.playlistEntity.playlistName
                // holder.text?.text = MusicUtil.playlistInfoString(activity, playlist.songs)
            }
            ALBUM_ARTIST -> {
                holder.imageTextContainer?.isVisible = true
                val artist = dataSet[position] as Artist
                holder.title?.text = artist.name
                holder.text?.text = MusicUtil.getArtistInfoString(activity, artist)
                GlideApp.with(activity).asDrawable().artistImageOptions(artist).load(
                    RetroGlideExtension.getArtistModel(artist)
                ).into(holder.image!!)
            }
            else -> {
                holder.title?.text = dataSet[position].toString()
                holder.title?.setTextColor(ThemeStore.accentColor(activity))
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView: View, itemViewType: Int) : MediaEntryViewHolder(itemView) {
        init {
            itemView.setOnLongClickListener(null)
            imageTextContainer?.isInvisible = true
            if (itemViewType == SONG) {
                imageTextContainer?.isGone = true
                menu?.isVisible = true
                menu?.setOnClickListener(object : SongMenuHelper.OnClickSongMenu(activity) {
                    override val song: Song
                        get() = dataSet[layoutPosition] as Song
                })
            } else {
                menu?.isVisible = false
            }

            when (itemViewType) {
                ALBUM -> setImageTransitionName(activity.getString(R.string.transition_album_art))
                ARTIST -> setImageTransitionName(activity.getString(R.string.transition_artist_image))
                else -> {
                    val container = itemView.findViewById<View>(R.id.imageContainer)
                    container?.isVisible = false
                }
            }
        }

        override fun onClick(v: View?) {
            val item = dataSet[layoutPosition]
            when (itemViewType) {
                ALBUM -> {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.albumDetailsFragment,
                        bundleOf(EXTRA_ALBUM_ID to (item as Album).id)
                    )
                }
                ARTIST -> {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.artistDetailsFragment,
                        bundleOf(EXTRA_ARTIST_ID to (item as Artist).id)
                    )
                }
                ALBUM_ARTIST -> {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.albumArtistDetailsFragment,
                        bundleOf(EXTRA_ARTIST_NAME to (item as Artist).name)
                    )
                }
                GENRE -> {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.genreDetailsFragment,
                        bundleOf(EXTRA_GENRE to (item as Genre))
                    )
                }
                PLAYLIST -> {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.playlistDetailsFragment,
                        bundleOf(EXTRA_PLAYLIST to (item as PlaylistWithSongs))
                    )
                }
                SONG -> {
                    MusicPlayerRemote.playNext(item as Song)
                    MusicPlayerRemote.playNextSong()
                }
            }
        }
    }

    companion object {
        private const val HEADER = 0
        private const val ALBUM = 1
        private const val ARTIST = 2
        private const val SONG = 3
        private const val GENRE = 4
        private const val PLAYLIST = 5
        private const val ALBUM_ARTIST = 6
    }
}
