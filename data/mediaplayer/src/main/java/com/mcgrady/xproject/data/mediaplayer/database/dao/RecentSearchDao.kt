package com.mcgrady.xproject.data.mediaplayer.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mcgrady.xproject.data.mediaplayer.database.entity.RecentSearch

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM recent_search ORDER BY search DESC")
    fun getRecent(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(search: RecentSearch)

    @Delete
    fun delete(search: RecentSearch)
}