package com.mcgrady.xproject.account.di

import com.mcgrady.xproject.account.network.AccountService
import com.mcgrady.xproject.account.repo.AccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideAccountRepository(accountService: AccountService): AccountRepository {
        return AccountRepository(accountService)
    }
}