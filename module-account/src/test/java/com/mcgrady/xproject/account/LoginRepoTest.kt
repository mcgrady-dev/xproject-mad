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