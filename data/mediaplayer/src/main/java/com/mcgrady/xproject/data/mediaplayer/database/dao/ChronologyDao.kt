package com.mcgrady.xproject.data.mediaplayer.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mcgrady.xproject.data.mediaplayer.database.entity.Chronology

@Dao
interface ChronologyDao {
    @Query("SELECT * FROM chronology WHERE timestamp >= :startDate AND timestamp < :endDate AND server == :server GROUP BY id ORDER BY COUNT(id) DESC LIMIT 9")
    fun getAllFrom(startDate: Long, endDate: Long, server: String?): LiveData<List<Chronology>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chronologyObject: Chronology)
}