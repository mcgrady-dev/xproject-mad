package com.mcgrady.xproject.data.entity


/**
 * Created by mcgrady on 2022/11/24.
 */
data class MainItemEntity(val id: Int, val name: String, val color: Int, val drawableText: String? = null) {

    companion object {
        const val ITEM_POKEMON_DB = 1
        const val ITEM_POKEMON = 2
        const val ITEM_ZHIHU = 3
        const val ITEM_MUSIC = 4
        const val ITEM_VIDEO = 5
        const val ITEM_CHAT = 6
    }
}
