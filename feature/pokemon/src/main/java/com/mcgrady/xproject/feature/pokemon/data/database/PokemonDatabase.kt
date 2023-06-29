package com.mcgrady.xproject.feature.pokemon.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mcgrady.xproject.feature.pokemon.data.database.dao.PokemonDao
import com.mcgrady.xproject.feature.pokemon.data.database.dao.PokemonInfoDao
import com.mcgrady.xproject.feature.pokemon.data.database.entity.PokemonEntity
import com.mcgrady.xproject.feature.pokemon.data.database.entity.PokemonInfoEntity
import com.mcgrady.xproject.feature.pokemon.data.database.util.TypeResponseConverter

@Database(
    entities = [PokemonEntity::class, PokemonInfoEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(value = [TypeResponseConverter::class])
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao

    abstract fun pokemonInfoDao(): PokemonInfoDao
}