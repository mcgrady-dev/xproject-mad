package com.mcgrady.xproject.data.mediaplayer.di

import android.app.Application
import androidx.room.Room
import com.mcgrady.xproject.data.mediaplayer.database.MusicDatabase
import com.mcgrady.xproject.data.mediaplayer.database.utils.DateConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicDatabaseModule {

    @Provides
    @Singleton
    fun provideMediaPlayerDatabase(application: Application, converter: DateConverters): MusicDatabase {
        return Room.databaseBuilder(application, MusicDatabase::class.java, MusicDatabase.DB_NAME)
            .fallbackToDestructiveMigration()
            .addTypeConverter(converter)
            .build()
    }
}