package com.mcgrady.xproject.feature.pokemon.ui.databinding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.core.base.adapter.BaseAdapter
import com.mcgrady.xproject.feature.pokemon.databinding.PokemonItemDetailRibbonBinding
import com.mcgrady.xproject.feature.pokemon.data.model.PokemonInfo

class PokemonDetailRibbonAdapter :
    BaseAdapter<PokemonInfo.TypeResponse, PokemonDetailRibbonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = PokemonItemDetailRibbonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        item: PokemonInfo.TypeResponse?
    ) {
        holder.bindTo(item)
    }

    class ViewHolder(
        private val binding: PokemonItemDetailRibbonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: PokemonInfo.TypeResponse?) {

            binding.tvName.text = item?.type?.name ?: ""
        }
    }
}
