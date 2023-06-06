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
package com.mcgrady.xproject.pokemon.ui.databinding.main

import android.os.Bundle
import androidx.activity.viewModels
import com.mcgrady.xarch.extension.viewBinding
import com.mcgrady.xproject.common.core.base.BaseActivity
import com.mcgrady.xproject.common.widget.transformationlayout.onTransformationStartContainer
import com.mcgrady.xproject.pokemon.databinding.PokemonActivityDatabindingListBinding
import com.mcgrady.xproject.pokemon.ui.databinding.adapter.PokemonDataBindingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDataBindingListActivity : BaseActivity() {

    private val binding by viewBinding(PokemonActivityDatabindingListBinding::inflate)
    private val viewModel: PokemonDataBindingViewModel by viewModels()

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

            lifecycleOwner = this@PokemonDataBindingListActivity
            adapter = PokemonDataBindingAdapter()
            vm = viewModel

//            val sizeProvider = ViewPreloadSizeProvider<Pokemon>()
//            val preloader = RecyclerViewPreloader(Glide.with(this@PokemonListActivity), adapter as ListPreloader.PreloadModelProvider<Pokemon>, sizeProvider, 5)
//            recyclerView.addOnScrollListener(preloader)
        }
    }
}
