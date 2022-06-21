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

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.google.android.material.button.MaterialButton
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.extensions.accentColor
import com.mcgrady.xproject.retromusic.extensions.accentOutlineColor
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import com.mcgrady.xproject.retromusic.interfaces.ICabHolder
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.retromusic.util.RetroUtil

class ShuffleButtonSongAdapter(
    activity: FragmentActivity,
    dataSet: MutableList<Song>,
    itemLayoutRes: Int,
    ICabHolder: ICabHolder?
) : AbsOffsetSongAdapter(activity, dataSet, itemLayoutRes, ICabHolder) {

    override fun createViewHolder(view: View): SongAdapter.ViewHolder {
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) OFFSET_ITEM else SONG
    }

    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        if (holder.itemViewType == OFFSET_ITEM) {
            val viewHolder = holder as ViewHolder
            viewHolder.playAction?.let {
                it.setOnClickListener {
                    MusicPlayerRemote.openQueue(dataSet, 0, true)
                }
                it.accentOutlineColor()
            }
            viewHolder.shuffleAction?.let {
                it.setOnClickListener {
                    MusicPlayerRemote.openAndShuffleQueue(dataSet, true)
                }
                it.accentColor()
            }
        } else {
            super.onBindViewHolder(holder, position - 1)
            val landscape = RetroUtil.isLandscape
            if ((PreferenceUtil.songGridSize > 2 && !landscape) || (PreferenceUtil.songGridSizeLand > 5 && landscape)) {
                holder.menu?.isVisible = false
            }
        }
    }

    inner class ViewHolder(itemView: View) : AbsOffsetSongAdapter.ViewHolder(itemView) {
        val playAction: MaterialButton? = itemView.findViewById(R.id.playAction)
        val shuffleAction: MaterialButton? = itemView.findViewById(R.id.shuffleAction)

        override fun onClick(v: View?) {
            if (itemViewType == OFFSET_ITEM) {
                MusicPlayerRemote.openAndShuffleQueue(dataSet, true)
                return
            }
            super.onClick(v)
        }
    }
}
