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
package com.mcgrady.xproject.core.network

import com.mcgrady.xproject.core.network.operators.IResponseOperator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okio.Timeout

object NetworkInitializer {

    @JvmStatic
    var successCodeRange: IntRange = 200..299

    @JvmStatic
    var responseOperators: MutableList<IResponseOperator> = mutableListOf()

    @JvmSynthetic
    var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    @JvmStatic
    var timeout: Timeout? = null
}
