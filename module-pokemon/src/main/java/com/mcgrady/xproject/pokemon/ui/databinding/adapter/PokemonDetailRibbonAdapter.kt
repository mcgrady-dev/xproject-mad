package com.mcgrady.xproject.pokemon.ui.databinding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcgrady.xproject.common.core.base.adapter.BaseAdapter
import com.mcgrady.xproject.pokemon.data.model.PokemonInfo
import com.mcgrady.xproject.pokemon.databinding.PokemonItemDetailRibbonBinding

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
