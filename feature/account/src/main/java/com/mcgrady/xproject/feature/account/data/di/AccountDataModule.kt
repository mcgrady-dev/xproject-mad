package com.mcgrady.xproject.feature.account.data.di

import com.mcgrady.xproject.feature.account.network.service.AccountService
import com.mcgrady.xproject.feature.account.data.repository.AccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class AccountDataModule {

    @Provides
    @ViewModelScoped
    fun provideAccountRepository(accountService: AccountService): AccountRepository {
        return AccountRepository(accountService)
    }
}