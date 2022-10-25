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
package com.mcgrady.xproject.pokemon.binding

import android.graphics.drawable.PictureDrawable
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import com.mcgrady.xproject.pokemon.network.GlideApp
import com.mcgrady.xproject.pokemon.network.imageload.SvgSoftwareLayerSetter

/**
 * Created by mcgrady on 2022/1/7.
 */
object ViewBinding {

    @JvmStatic
    @BindingAdapter("toast")
    fun bindToast(view: View, text: String?) {
        text?.let {
            Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    @BindingAdapter("paletteImage", "paletteCard")
    fun bindLoadImagePaletteView(view: AppCompatImageView, url: String, paletteCard: MaterialCardView) {

        GlideApp.with(view)
            .`as`(PictureDrawable::class.java)
            .listener(SvgSoftwareLayerSetter())
            .load(url)
            .into(view)

//        Glide.with(view)
//            .asBitmap()
//            .load(url)
//            .listener(object: RequestListener<Bitmap> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Bitmap>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Bitmap?,
//                    model: Any?,
//                    target: Target<Bitmap>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    resource?.let { bitmap ->
//                        Palette.from(bitmap)
//                            .generate { palette ->
//                                palette?.dominantSwatch?.rgb?.let {
//                                    paletteCard.setCardBackgroundColor(it)
//                                }
//                            }
//                    }
//                    return false
//                }
//            }).into(view)
    }

    @JvmStatic
    @BindingAdapter("gone")
    fun bindGone(view: View, shouldBeGone: Boolean) {
        view.visibility = if (shouldBeGone) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}
