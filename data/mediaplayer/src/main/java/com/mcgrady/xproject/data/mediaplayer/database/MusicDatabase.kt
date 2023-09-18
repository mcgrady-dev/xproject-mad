package com.mcgrady.xproject.data.mediaplayer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mcgrady.xproject.data.mediaplayer.database.dao.ChronologyDao
import com.mcgrady.xproject.data.mediaplayer.database.dao.DownloadDao
import com.mcgrady.xproject.data.mediaplayer.database.dao.FavoriteDao
import com.mcgrady.xproject.data.mediaplayer.database.dao.QueueDao
import com.mcgrady.xproject.data.mediaplayer.database.dao.RecentSearchDao
import com.mcgrady.xproject.data.mediaplayer.database.dao.ServerDao
import com.mcgrady.xproject.data.mediaplayer.database.entity.Chronology
import com.mcgrady.xproject.data.mediaplayer.database.entity.Download
import com.mcgrady.xproject.data.mediaplayer.database.entity.Favorite
import com.mcgrady.xproject.data.mediaplayer.database.entity.Queue
import com.mcgrady.xproject.data.mediaplayer.database.entity.RecentSearch
import com.mcgrady.xproject.data.mediaplayer.database.entity.Server
import com.mcgrady.xproject.data.mediaplayer.database.utils.DateConverters


@Database(
    version = 1,
    entities = [Queue::class, Server::class, RecentSearch::class, Download::class, Chronology::class, Favorite::class],
    exportSchema = true
)
@TypeConverters(value = [DateConverters::class])
abstract class MusicDatabase : RoomDatabase() {

    abstract fun queueDao(): QueueDao

    abstract fun serverDao(): ServerDao

    abstract fun recentSearchDao(): RecentSearchDao

    abstract fun downloadDao(): DownloadDao

    abstract fun chronologyDao(): ChronologyDao

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        const val DB_NAME = "music_db"
    }
}