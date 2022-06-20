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