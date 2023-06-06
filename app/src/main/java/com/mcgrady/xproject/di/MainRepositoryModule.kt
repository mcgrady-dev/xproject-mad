package com.mcgrady.xproject.di

import com.mcgrady.xproject.repo.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object MainRepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideSettingRepository(): MainRepository {
        return MainRepository()
    }

}