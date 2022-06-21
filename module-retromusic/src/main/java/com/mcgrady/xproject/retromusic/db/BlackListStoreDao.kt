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
package com.mcgrady.xproject.retromusic.db

import androidx.room.*

@Dao
interface BlackListStoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBlacklistPath(blackListStoreEntity: BlackListStoreEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlacklistPath(blackListStoreEntities: List<BlackListStoreEntity>)

    @Delete
    suspend fun deleteBlacklistPath(blackListStoreEntity: BlackListStoreEntity)

    @Query("DELETE FROM BlackListStoreEntity")
    suspend fun clearBlacklist()

    @Query("SELECT * FROM BlackListStoreEntity")
    fun blackListPaths(): List<BlackListStoreEntity>
}
