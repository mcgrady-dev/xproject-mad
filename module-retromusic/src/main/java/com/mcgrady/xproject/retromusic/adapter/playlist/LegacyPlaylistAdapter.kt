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
package com.mcgrady.xproject.retromusic.adapter.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.retromusic.adapter.base.MediaEntryViewHolder
import com.mcgrady.xproject.retromusic.model.Playlist
import com.mcgrady.xproject.retromusic.util.MusicUtil

class LegacyPlaylistAdapter(
    private val activity: FragmentActivity,
    private var list: List<Playlist>,
    private val layoutRes: Int,
    private val playlistClickListener: PlaylistClickListener
) :
    RecyclerView.Adapter<LegacyPlaylistAdapter.ViewHolder>() {

    fun swapData(list: List<Playlist>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist: Playlist = list[position]
        holder.title?.text = playlist.name
        holder.text?.text = MusicUtil.getPlaylistInfoString(activity, playlist.getSongs())
        holder.itemView.setOnClickListener {
            playlistClickListener.onPlaylistClick(playlist)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}
