package com.mcgrady.xproject.data.mediaplayer.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "recent_search")
data class RecentSearch(
    @PrimaryKey
    @ColumnInfo(name = "search")
    var search: String
) : Parcelable
