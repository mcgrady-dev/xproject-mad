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

import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.google.android.material.card.MaterialCardView

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
    val context = view.context
    Glide.with(context)
      .load(url)
      .listener(
          GlidePalette.with(url)
            .use(BitmapPalette.Profile.MUTED_LIGHT)
            .intoCallBack { palette ->
                val rgb = palette?.dominantSwatch?.rgb
              if (rgb != null) {
                paletteCard.setCardBackgroundColor(rgb)
              }
              }.crossfade(true)
      ).into(view)
  }

//    @JvmStatic
//    @BindingAdapter("paletteImage", "paletteView")
//    fun bindLoadImagePaletteView(view: AppCompatImageView, url: String, paletteView: View) {
//        val context = view.context
//        Glide.with(context)
//            .load(url)
//            .listener(
//                GlidePalette.with(url)
//                    .use(BitmapPalette.Profile.MUTED_LIGHT)
//                    .intoCallBack { palette ->
//                        val light = palette?.lightVibrantSwatch?.rgb
//                        val domain = palette?.dominantSwatch?.rgb
//                        if (domain != null) {
// //                            if (light != null) {
// //                                Rainbow(paletteView).palette {
// //                                    +color(domain)
// //                                    +color(light)
// //                                }.background(orientation = RainbowOrientation.TOP_BOTTOM)
// //                            } else {
// //                                paletteView.setBackgroundColor(domain)
// //                            }
//
//                            paletteView.setBackgroundColor(domain)
//
//                            if (context is AppCompatActivity) {
//                                context.window.apply {
//                                    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                                    statusBarColor = domain
//                                }
//                            }
//                        }
//                    }.crossfade(true)
//            ).into(view)
//    }

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
