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
package com.mcgrady.xproject.pokemon.ui.adapter

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.mcgrady.xarchitecture.ext.databind
import com.mcgrady.xproject.common.widget.extensions.inflate
import com.mcgrady.xproject.common.widget.transformationlayout.TransformationCompat
import com.mcgrady.xproject.pokemon.R
import com.mcgrady.xproject.pokemon.databinding.ItemPokemonBinding
import com.mcgrady.xproject.pokemon.model.Pokemon
import com.mcgrady.xproject.pokemon.ui.main.MainActivity

/**
 * Created by mcgrady on 2022/1/7.
 */
class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

  private val items: MutableList<Pokemon> = mutableListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
    val view = parent.inflate(R.layout.item_pokemon)
    return PokemonViewHolder(view).apply {
//            binding.root.setOnClickListener(object: ClickUtils.OnDebouncingClickListener() {
//                override fun onDebouncingClick(v: View?) {
//                    val position = bindingAdapterPosition.takeIf { it != NO_POSITION } ?: return
//                    val intent = Intent(v?.context, MainActivity::class.java).apply {
//                        putExtra("EXTRA_POKEMON", items[position])
//                    }
//                    TransformationCompat.startActivity(binding.transformationLayout, intent)
//                }
//            })

      binding.root.setOnClickListener { v ->
        val position = bindingAdapterPosition.takeIf { it != NO_POSITION } ?: return@setOnClickListener
        val intent = Intent(v?.context, MainActivity::class.java).apply {
          putExtra("EXTRA_POKEMON", items[position])
        }
        TransformationCompat.startActivity(binding.transformationLayout, intent)
      }
    }
  }

  override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
    holder.bindData(items[position], position)
  }

  override fun getItemCount() = items.size

  fun setPokemonList(list: List<Pokemon>) {
    val previousItemSize = items.size
    items.clear()
    items.addAll(list)
    notifyItemRangeChanged(previousItemSize, list.size)
  }

  class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding: ItemPokemonBinding by databind()

    @Suppress("UNUSED_PARAMETER")
    fun bindData(data: Pokemon?, position: Int) {
      binding.apply {
        pokemon = data
        executePendingBindings()
      }
    }
  }
}
