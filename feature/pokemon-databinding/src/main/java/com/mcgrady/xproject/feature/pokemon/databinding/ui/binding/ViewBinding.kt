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
package com.mcgrady.xproject.feature.pokemon.databinding.ui.binding

import android.graphics.Bitmap
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.mcgrady.xproject.core.ui.gradient.rainbow.Rainbow
import com.mcgrady.xproject.core.ui.gradient.rainbow.RainbowOrientation
import com.mcgrady.xproject.core.ui.gradient.rainbow.color
import com.mcgrady.xproject.core.ui.progressview.ProgressView
import com.mcgrady.xproject.data.pokemon.model.PokemonInfo

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
    fun bindLoadImagePalette(view: AppCompatImageView, url: String, paletteCard: MaterialCardView) {
        Glide.with(view)
//            .`as`(PictureDrawable::class.java)
//            .listener(SvgSoftwareLayerSetter())
            .asBitmap()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    paletteCard.setCardBackgroundColor(ContextCompat.getColor(view.context, com.mcgrady.xproject.core.ui.R.color.background800))
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    resource?.let { bitmap ->
                        Palette.from(bitmap)
                            .generate { palette ->
                                palette?.dominantSwatch?.rgb?.let {
                                    paletteCard.setCardBackgroundColor(it)
                                }
                            }
                    }
                    return false
                }
            })
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("paletteImage", "paletteView")
    fun bindLoadImagePaletteView(view: AppCompatImageView, url: String, paletteView: View) {
        val context = view.context
        Glide.with(view)
            .asBitmap()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    paletteView.setBackgroundColor(ContextCompat.getColor(view.context, com.mcgrady.xproject.core.ui.R.color.background800))
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    resource?.let { bitmap ->
                        Palette.from(bitmap)
                            .generate { palette ->
                                val light = palette?.lightMutedSwatch?.rgb
                                val dominant = palette?.dominantSwatch?.rgb
                                if (dominant != null) {
                                    if (light != null) {
                                        Rainbow(paletteView).palette {
                                            +color(dominant)
                                            +color(light)
                                        }.background(orientation = RainbowOrientation.TOP_BOTTOM)
                                    } else {
                                        paletteView.setBackgroundColor(dominant)
                                    }
                                    if (context is AppCompatActivity) {
                                        context.window.apply {
                                            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                                            statusBarColor = dominant
                                        }
                                    }
                                }
                            }
                    }
                    return false
                }
            })
            .into(view)
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

    @JvmStatic
    @BindingAdapter("onBackPressed")
    fun bindOnBackPressed(view: View, onBackPress: Boolean) {
        val context = view.context
        if (onBackPress && context is OnBackPressedDispatcherOwner) {
            view.setOnClickListener {
                context.onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("bindPokemonTypes")
    fun bindPokemonTypes(recyclerView: RecyclerView, types: List<PokemonInfo.TypeResponse>?) {
        types?.let {
            for (type in it) {
                with(recyclerView) {
//                    addRibbon(
//                        ribbonView(context) {
//                            setText(type.type.name)
//                            setTextColor(Color.WHITE)
//                            setPaddingLeft(84f)
//                            setPaddingRight(84f)
//                            setPaddingTop(2f)
//                            setPaddingBottom(10f)
//                            setTextSize(16f)
//                            setRibbonRadius(120f)
//                            setTextStyle(Typeface.BOLD)
//                            setRibbonBackgroundColorResource(
//                                PokemonTypeUtils.getTypeColor(type.type.name)
//                            )
//                        }.apply {
//                            maxLines = 1
//                            gravity = Gravity.CENTER
//                        }
//                    )
//                    addItemDecoration(SpacesItemDecoration())
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("progressView_labelText")
    fun bindProgressViewLabelText(progressView: ProgressView, text: String?) {
        progressView.labelText = text
    }

    @JvmStatic
    @BindingAdapter("progressView_progress")
    fun bindProgressViewProgress(progressView: ProgressView, value: Int?) {
        if (value != null) {
            progressView.progress = value.toFloat()
        }
    }

    @JvmStatic
    @BindingAdapter("progressView_max")
    fun bindProgressViewMax(progressView: ProgressView, value: Int?) {
        if (value != null) {
            progressView.max = value.toFloat()
        }
    }
}
