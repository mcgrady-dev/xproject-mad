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
package com.mcgrady.xproject

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.mcgrady.xproject.common.core.app.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by mcgrady on 2021/5/10.
 */
@HiltAndroidApp
class App : BaseApplication(), LifecycleEventObserver {

  override fun onCreate() {
    super.onCreate()
  }

  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    when (event) {
      Lifecycle.Event.ON_START -> {
        Timber.i("LifecycleObserver: 应用回到前台")
      }
      Lifecycle.Event.ON_STOP -> {
        Timber.i("LifecycleObserver: 应用退到后台")
      }
      else -> {}
    }
  }
}
