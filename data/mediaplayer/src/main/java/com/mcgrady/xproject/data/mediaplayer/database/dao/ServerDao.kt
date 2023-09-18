package com.mcgrady.xproject.data.mediaplayer.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mcgrady.xproject.data.mediaplayer.database.entity.Server

@Dao
interface ServerDao {
    @Query("SELECT * FROM server")
    fun getALl(): LiveData<List<Server>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(server: Server)

    @Delete
    fun delete(server: Server)
}