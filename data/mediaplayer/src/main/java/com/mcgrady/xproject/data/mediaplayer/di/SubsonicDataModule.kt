package com.mcgrady.xproject.data.mediaplayer.di

import com.mcgrady.xproject.data.mediaplayer.extensions.subsonicServer
import com.mcgrady.xproject.core.utils.PreferenceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//class SubsonicDataModule {
//
//    @Provides
//    @Singleton
//    fun provideSubsonicClient() {
//        PreferenceUtil.subsonicServer
//    }
//}