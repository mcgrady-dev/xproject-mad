package com.mcgrady.xproject.feature.account.data.repository

import com.mcgrady.xproject.feature.account.network.service.AccountService
import com.mcgrady.xproject.core.repo.Repository
import javax.inject.Inject

class AccountRepository @Inject constructor(private val service: AccountService) : Repository {

    fun fetchUsers() = service.fetchUsers()

    fun fetchUserById(userId: Int) = service.fetchUserById(userId)

    suspend fun fetchUsersSuspend() = service.fetchUsersSuspend()

    suspend fun fetchUserByIdSuspend(userId: Int) = service.fetchUserByIdSuspend(userId)

}