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
package com.mcgrady.xproject.retromusic.repository

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mcgrady.xproject.retromusic.model.Contributor

interface LocalDataRepository {
    fun contributors(): List<Contributor>
}

class RealLocalDataRepository(
    private val context: Context
) : LocalDataRepository {

    override fun contributors(): List<Contributor> {
        val jsonString = context.assets.open("contributors.json")
            .bufferedReader().use { it.readText() }

        val gsonBuilder = GsonBuilder()
        val gson = gsonBuilder.create()
        val listContributorType = object : TypeToken<List<Contributor>>() {}.type
        return gson.fromJson(jsonString, listContributorType)
    }
}
