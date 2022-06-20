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
