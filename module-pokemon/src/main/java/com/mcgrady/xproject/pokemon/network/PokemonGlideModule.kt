package com.mcgrady.xproject.pokemon.network

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.mcgrady.xproject.pokemon.BuildConfig

/**
 * Created by mcgrady on 2022/6/28.
 */
@GlideModule
class PokedexGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setLogLevel(if (BuildConfig.DEBUG) Log.DEBUG else Log.INFO)
    }
}