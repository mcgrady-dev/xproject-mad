/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject.feature.account.data.repository

import com.mcgrady.xproject.core.repo.Repository
import com.mcgrady.xproject.feature.account.network.service.AccountService
import javax.inject.Inject

class AccountRepository @Inject constructor(private val service: AccountService) : Repository {

    fun fetchUsers() = service.fetchUsers()

    fun fetchUserById(userId: Int) = service.fetchUserById(userId)

    suspend fun fetchUsersSuspend() = service.fetchUsersSuspend()

    suspend fun fetchUserByIdSuspend(userId: Int) = service.fetchUserByIdSuspend(userId)
}
