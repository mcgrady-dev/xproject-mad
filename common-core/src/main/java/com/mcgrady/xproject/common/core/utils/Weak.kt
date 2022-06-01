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
package com.mcgrady.xproject.common.core.utils

import timber.log.Timber
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2022/1/4.
 * any by Weak
 */
class Weak<T : Any>(initializer: () -> T?) {
  var weakReference = WeakReference<T?>(initializer())

  constructor() : this({
    null
  })

  operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
    Timber.d("Weak Delegate", "getValue")
    return weakReference.get()
  }

  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
    Timber.d("Weak Delegate", "setValue")
    weakReference = WeakReference(value)
  }
}
