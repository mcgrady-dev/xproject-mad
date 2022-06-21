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
package com.mcgrady.xproject.retromusic.util

import android.util.Log
import com.mcgrady.xproject.retromusic.BuildConfig

fun Any.logD(message: Any?) {
    logD(message.toString())
}

fun Any.logD(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(name, message)
    }
}

fun Any.logE(message: String) {
    Log.e(name, message)
}

fun Any.logE(e: Exception) {
    Log.e(name, e.message ?: "Error")
}

private val Any.name: String get() = this::class.java.simpleName
