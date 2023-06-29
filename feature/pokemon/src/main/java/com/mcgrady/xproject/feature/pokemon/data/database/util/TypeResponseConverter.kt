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
package com.mcgrady.xproject.feature.pokemon.data.database.util

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.mcgrady.xproject.feature.pokemon.data.model.PokemonInfo
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
                string = value,
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
