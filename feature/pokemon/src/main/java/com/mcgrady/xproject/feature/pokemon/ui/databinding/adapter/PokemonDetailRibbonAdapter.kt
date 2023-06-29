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
package com.mcgrady.xproject.feature.pokemon.ui.databinding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.core.base.adapter.BaseAdapter
import com.mcgrady.xproject.feature.pokemon.data.model.PokemonInfo
import com.mcgrady.xproject.feature.pokemon.databinding.PokemonItemDetailRibbonBinding

class PokemonDetailRibbonAdapter :
    BaseAdapter<PokemonInfo.TypeResponse, PokemonDetailRibbonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = PokemonItemDetailRibbonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        item: PokemonInfo.TypeResponse?,
    ) {
        holder.bindTo(item)
    }

    class ViewHolder(
        private val binding: PokemonItemDetailRibbonBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: PokemonInfo.TypeResponse?) {
            binding.tvName.text = item?.type?.name ?: ""
        }
    }
}
