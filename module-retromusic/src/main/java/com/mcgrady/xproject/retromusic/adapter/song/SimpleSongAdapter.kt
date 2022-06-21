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
package com.mcgrady.xproject.retromusic.adapter.song

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.mcgrady.xproject.retromusic.interfaces.ICabHolder
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.util.MusicUtil

class SimpleSongAdapter(
    context: FragmentActivity,
    songs: ArrayList<Song>,
    layoutRes: Int,
    ICabHolder: ICabHolder?
) : SongAdapter(context, songs, layoutRes, ICabHolder) {

    override fun swapDataSet(dataSet: List<Song>) {
        this.dataSet = dataSet.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val fixedTrackNumber = MusicUtil.getFixedTrackNumber(dataSet[position].trackNumber)

        holder.imageText?.text = if (fixedTrackNumber > 0) fixedTrackNumber.toString() else "-"
        holder.time?.text = MusicUtil.getReadableDurationString(dataSet[position].duration)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
