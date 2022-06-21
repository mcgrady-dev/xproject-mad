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

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    companion object {
        private const val HISTORY_LIMIT = 100
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongInHistory(historyEntity: HistoryEntity)

    @Query("DELETE FROM HistoryEntity WHERE id= :songId")
    fun deleteSongInHistory(songId: Long)
    @Query("SELECT * FROM HistoryEntity WHERE id = :songId LIMIT 1")
    suspend fun isSongPresentInHistory(songId: Long): HistoryEntity?

    @Update
    suspend fun updateHistorySong(historyEntity: HistoryEntity)

    @Query("SELECT * FROM HistoryEntity ORDER BY time_played DESC LIMIT $HISTORY_LIMIT")
    fun historySongs(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity ORDER BY time_played DESC LIMIT $HISTORY_LIMIT")
    fun observableHistorySongs(): LiveData<List<HistoryEntity>>

    @Query("DELETE FROM HistoryEntity")
    suspend fun clearHistory()
}
