package com.mcgrady.xproject.account.data

import com.mcgrady.xproject.account.data.model.LoggedInUser

/**
 * Created by mcgrady on 2022/6/10.
 */
interface ILoginRepo {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser?

    fun login(username: String, password: String): Result<LoggedInUser>
}