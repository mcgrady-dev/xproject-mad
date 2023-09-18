package com.mcgrady.xproject.data.mediaplayer.repository

import com.mcgrady.xproject.core.repo.Repository
import dagger.Provides
import javax.inject.Singleton

interface MediaSongRepository : Repository {

    fun provideSubsonicClient()

}