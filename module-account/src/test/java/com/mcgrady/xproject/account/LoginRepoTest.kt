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
package com.mcgrady.xproject.account

import com.google.common.truth.Truth
import com.mcgrady.xproject.account.data.ILoginRepo
import com.mcgrady.xproject.account.data.Result
import com.mcgrady.xproject.account.data.model.LoggedInUser
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by mcgrady on 2022/6/10.
 */
class LoginRepoTest {

    @MockK
    lateinit var repo: ILoginRepo

    lateinit var loginResponseSuccess: Result<LoggedInUser>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginResponseSuccess = Result.Success(LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe"))
    }

    @After
    fun clearUp() {
    }

    @Test
    fun `validate login is success`() {
        runBlocking {
            every { repo.login("mcgrady", "123") } returns loginResponseSuccess
        }

        runBlocking {
            Truth.assertThat(repo.login("mcgrady", "123"))
                .isEqualTo(loginResponseSuccess)
        }
    }
}
