package com.mcgrady.xproject.account.repo

import com.mcgrady.xproject.account.network.AccountService
import com.mcgrady.xproject.common.core.repo.Repository
import javax.inject.Inject

class AccountRepository @Inject constructor(private val service: AccountService) : Repository {

    fun fetchUsers() = service.fetchUsers()

    fun fetchUserById(userId: Int) = service.fetchUserById(userId)

    suspend fun fetchUsersSuspend() = service.fetchUsersSuspend()

    suspend fun fetchUserByIdSuspend(userId: Int) = service.fetchUserByIdSuspend(userId)

}