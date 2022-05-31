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