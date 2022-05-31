package com.mcgrady.xproject.di

import android.content.Context
import com.mcgrady.xproject.repo.SettingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Created by mcgrady on 2022/5/31.
 */
@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideSettingRepository(@ApplicationContext context: Context): SettingRepository {
        return SettingRepository(context)
    }
}