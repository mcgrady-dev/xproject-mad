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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.mcgrady.xproject.account.data.LoginDataSource
import com.mcgrady.xproject.account.data.LoginRepository
import com.mcgrady.xproject.account.data.Result
import com.mcgrady.xproject.account.data.model.LoggedInUser
import com.mcgrady.xproject.account.ui.login.LoggedInUserView
import com.mcgrady.xproject.account.ui.login.LoginResult
import com.mcgrady.xproject.account.ui.login.LoginViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by mcgrady on 2022/6/10.
 */
@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var dataSource: LoginDataSource
    lateinit var repo: LoginRepository
    lateinit var viewModel: LoginViewModel

    val dispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        repo = LoginRepository(dataSource)
        viewModel = LoginViewModel(repo)
    }

    @After
    fun clearUp() {
        Dispatchers.resetMain()
        dispatcher.cancel()
    }

    @Test
    fun `respnse received check fialed state`() {
        every { repo.login("mcgrady", "123") } returns Result.Error("null")
        viewModel.login("mcgrady", "123")

        Truth.assertThat(viewModel.loginResult.value).isEqualTo(LoginResult(error = R.string.login_failed))
    }

    @Test
    fun `respnse received check success state`() {
        every { dataSource.login("mcgrady", "123") } returns Result.Success(LoggedInUser("1", "mcgrady"))
        every { repo.login("mcgrady", "123") } returns Result.Success(LoggedInUser("1", "mcgrady"))
        viewModel.login("mcgrady", "123")

        Truth.assertThat(dataSource.login("mcgrady", "123")).isEqualTo(Result.Success(LoggedInUser("1", "mcgrady")))
        Truth.assertThat(repo.user).isEqualTo(LoggedInUser("1", "mcgrady"))
        Truth.assertThat(viewModel.loginResult.value).isEqualTo(LoginResult(success = LoggedInUserView(displayName = "mcgrady")))
    }
}
