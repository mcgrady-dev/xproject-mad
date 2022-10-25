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
package com.mcgrady.xproject.pokemon.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import com.mcgrady.xarch.ext.databind
import com.mcgrady.xproject.common.core.base.BaseActivity
import com.mcgrady.xproject.common.widget.transformationlayout.onTransformationStartContainer
import com.mcgrady.xproject.pokemon.R
import com.mcgrady.xproject.pokemon.databinding.ActivityPokemonListBinding
import com.mcgrady.xproject.pokemon.ui.adapter.PokemonAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonListActivity : BaseActivity() {

    private val binding: ActivityPokemonListBinding by databind(R.layout.activity_pokemon_list)
    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)

        binding.apply {

            /*val preloadSizeProvider: FixedPreloadSizeProvider<Photo> =
                FixedPreloadSizeProvider<Photo>(photoSize, photoSize)
            val preloader: RecyclerViewPreloader<Pokemon> = RecyclerViewPreloader<Any?>(
                Glide.with(this),
                adapter,
                preloadSizeProvider,
                args.getInt(com.bumptech.glide.samples.flickr.FlickrPhotoGrid.PRELOAD_KEY)
            )
            grid.addOnScrollListener(preloader)*/

            lifecycleOwner = this@PokemonListActivity
            adapter = PokemonAdapter()
            vm = viewModel

//            val sizeProvider = ViewPreloadSizeProvider<Pokemon>()
//            val preloader = RecyclerViewPreloader(Glide.with(this@PokemonListActivity), adapter as ListPreloader.PreloadModelProvider<Pokemon>, sizeProvider, 5)
//            recyclerView.addOnScrollListener(preloader)
        }
    }
}
