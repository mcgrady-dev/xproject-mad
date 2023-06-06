package com.mcgrady.xproject.repo

import com.mcgrady.xproject.common.core.repo.Repository
import com.mcgrady.xproject.common.core.utils.XUtils
import com.mcgrady.xproject.data.entity.MainItemEntity

class MainRepository : Repository {

    fun fetchMainItemEntities(): List<MainItemEntity> = arrayListOf(
        MainItemEntity(MainItemEntity.ITEM_POKEMON_DB, "Pokemon\nDataBinding", XUtils.getRandomColor(false)),
        MainItemEntity(MainItemEntity.ITEM_POKEMON, "Pokemon", XUtils.getRandomColor(false)),
        MainItemEntity(MainItemEntity.ITEM_ZHIHU, "Zhihu Daily", XUtils.getRandomColor(false)),
        MainItemEntity(MainItemEntity.ITEM_MUSIC, "Retro Music", XUtils.getRandomColor(false)),
        MainItemEntity(MainItemEntity.ITEM_VIDEO, "Video Player", XUtils.getRandomColor(false)),
        MainItemEntity(MainItemEntity.ITEM_CHAT, "Chat", XUtils.getRandomColor(false)),
    )
}