package com.mcgrady.xproject.common_core.utils

import com.mcgrady.xproject.common_core.log.Log
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
        Log.d("Weak Delegate", "getValue")
        return weakReference.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        Log.d("Weak Delegate", "setValue")
        weakReference = WeakReference(value)
    }
}