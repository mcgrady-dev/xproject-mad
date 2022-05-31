package com.mcgrady.xproject.pokemon.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by mcgrady on 2022/1/7.
 */
@Parcelize
data class Pokemon(var page: Int = 0, var name: String, val url: String) : Parcelable {

    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://pokeres.bastionbot.org/images/pokemon/$index.png"
    }
}
