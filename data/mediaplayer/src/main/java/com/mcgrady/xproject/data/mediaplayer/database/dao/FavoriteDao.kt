package com.mcgrady.xproject.data.mediaplayer.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mcgrady.xproject.data.mediaplayer.database.entity.Favorite

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getAll(): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("DELETE FROM favorite")
    fun deleteAll()
}