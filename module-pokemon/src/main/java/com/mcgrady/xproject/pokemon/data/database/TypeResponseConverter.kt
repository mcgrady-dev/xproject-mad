package com.mcgrady.xproject.pokemon.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.mcgrady.xproject.pokemon.data.model.PokemonInfo
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.Exception

@ProvidedTypeConverter
class TypeResponseConverter @Inject constructor(private val json: Json) {

    @TypeConverter
    fun fromString(value: String): List<PokemonInfo.TypeResponse>? {
        return try {
            json.decodeFromString(
                deserializer = ListSerializer(PokemonInfo.TypeResponse.serializer()),
                string = value
            )
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun fromInfoType(type: List<PokemonInfo.TypeResponse>?): String {
        return try {
            json.encodeToString(type)
        } catch (e: Exception) {
            ""
        }
    }
}