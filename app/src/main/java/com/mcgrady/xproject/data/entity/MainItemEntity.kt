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
