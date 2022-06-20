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
import com.mcgrady.xproject.account.data.LoginDataSource
import com.mcgrady.xproject.account.data.Result
import com.mcgrady.xproject.account.data.model.LoggedInUser
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

/**
 * Created by mcgrady on 2022/6/10.
 */
class LoginDataSourceTest {

    @MockK
    lateinit var dataSource: LoginDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `validate login return success`() {
        every { dataSource.login("mcgrady", "123") } returns Result.Success(LoggedInUser("1", "mcgrady"))

        Truth.assertThat(dataSource.login("mcgrady", "123")).isEqualTo(Result.Success(LoggedInUser("1", "mcgrady")))
    }

    @Test
    fun `validate login return error`() {
        every { dataSource.login("mcgrady", "123") } returns Result.Error("Error logging in")

        Truth.assertThat(dataSource.login("mcgrady", "123")).isEqualTo(Result.Error("Error logging in"))
    }
}
