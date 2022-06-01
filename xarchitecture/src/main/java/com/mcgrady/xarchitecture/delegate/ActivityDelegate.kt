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
package com.mcgrady.xarchitecture.delegate

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.ext.observerWhenDestroyed
import kotlin.properties.ReadOnlyProperty

/**
 * Created by mcgrady on 2021/7/19.
 */
abstract class ActivityDelegate<T : ViewBinding>(activity: Activity) :
    ReadOnlyProperty<Activity, T> {

    protected var binding: T? = null

    init {
        when (activity) {
            is ComponentActivity -> activity.lifecycle.observerWhenDestroyed { destroyed() }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    activity.observerWhenDestroyed { destroyed() }
                }
            }
        }
    }

    protected fun destroyed() {
        binding = null
    }

    companion object {
        const val LIFECYCLE_FRAGMENT_TAG = "com.mcgrady.xarchitecture.lifecycle_fragment"
    }
}
