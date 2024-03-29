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
package com.mcgrady.xproject.core.base

import android.app.Application
import android.os.Build
import com.mcgrady.libs.core.extensions.internalAppCachePath
import com.mcgrady.xproject.core.common.BuildConfig
import com.mcgrady.xproject.core.lifecycle.ActivityLifecycleCallbacksImpl
import com.mcgrady.xproject.core.log.tree.FileLoggingTree
import com.mcgrady.xproject.core.log.tree.ReleaseTree
import com.mcgrady.xproject.core.utils.PreferenceUtil
import timber.log.Timber

/**
 * Created by mcgrady on 2021/12/16.
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
            Timber.plant(FileLoggingTree(internalAppCachePath()))
        }

        PreferenceUtil.init(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())
        }
    }

    companion object {
        lateinit var instance: Application
    }
}
