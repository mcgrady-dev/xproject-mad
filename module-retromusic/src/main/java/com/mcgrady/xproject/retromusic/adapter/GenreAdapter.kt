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
import android.view.ViewOutlineProvider
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.ItemGenreBinding
import com.mcgrady.xproject.retromusic.glide.GlideApp
import com.mcgrady.xproject.retromusic.glide.RetroGlideExtension
import com.mcgrady.xproject.retromusic.glide.RetroMusicColoredTarget
import com.mcgrady.xproject.retromusic.interfaces.IGenreClickListener
import com.mcgrady.xproject.retromusic.model.Genre
import com.mcgrady.xproject.retromusic.util.MusicUtil
import com.mcgrady.xproject.retromusic.util.color.MediaNotificationProcessor
import java.util.*

/**
 * @author Hemanth S (h4h13).
 */

class GenreAdapter(
    private val activity: FragmentActivity,
    var dataSet: List<Genre>,
    private val listener: IGenreClickListener
) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    init {
        this.setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGenreBinding.inflate(LayoutInflater.from(activity), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = dataSet[position]
        holder.binding.title.text = genre.name
        holder.binding.text.text = String.format(
            Locale.getDefault(),
            "%d %s",
            genre.songCount,
            if (genre.songCount > 1) activity.getString(R.string.songs) else activity.getString(R.string.song)
        )
        loadGenreImage(genre, holder)
    }

    private fun loadGenreImage(genre: Genre, holder: GenreAdapter.ViewHolder) {
        val genreSong = MusicUtil.songByGenre(genre.id)
        GlideApp.with(activity)
            .asBitmapPalette()
            .load(RetroGlideExtension.getSongModel(genreSong))
            .songCoverOptions(genreSong)
            .into(object : RetroMusicColoredTarget(holder.binding.image) {
                override fun onColorReady(colors: MediaNotificationProcessor) {
                    setColors(holder, colors)
                }
            })
        // Just for a bit of shadow around image
        holder.binding.image.outlineProvider = ViewOutlineProvider.BOUNDS
    }

    private fun setColors(holder: ViewHolder, color: MediaNotificationProcessor) {
        holder.binding.imageContainerCard.setCardBackgroundColor(color.backgroundColor)
        holder.binding.title.setTextColor(color.primaryTextColor)
        holder.binding.text.setTextColor(color.secondaryTextColor)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun swapDataSet(list: List<Genre>) {
        dataSet = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        override fun onClick(v: View?) {
            listener.onClickGenre(dataSet[layoutPosition], itemView)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}
