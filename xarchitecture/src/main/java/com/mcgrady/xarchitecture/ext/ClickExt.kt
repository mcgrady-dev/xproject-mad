@file:Suppress("NOTHING_TO_INLINE")

package com.mcgrady.xarchitecture.ext

import android.view.View
import androidx.annotation.CheckResult
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.CancellationException

/**
 * Created by mcgrady on 2022/5/31.
 */
//@kotlin.internal.InlineOnly
inline fun <E> SendChannel<E>.safeOffer(value: E) = !isClosedForSend && try {
    trySend(value).isSuccess
} catch (e: CancellationException) {
    false
}

@CheckResult
//@kotlin.internal.InlineOnly
inline fun View.clickFlow(): Flow<View> {
    return callbackFlow {
        setOnClickListener {
            safeOffer(it)
        }
        awaitClose { setOnClickListener(null) }
    }
}

//@kotlin.internal.InlineOnly
inline fun View.click(lifecycle: LifecycleCoroutineScope, noinline onClick: (view: View) -> Unit) {
    clickFlow().onEach {
        onClick(this)
    }.launchIn(lifecycle)
}

//延迟第一次点击事件
//@kotlin.internal.InlineOnly
inline fun View.clickDelayed(
    lifecycle: LifecycleCoroutineScope,
    delayMillis: Long = 500,
    noinline onClick: (view: View) -> Unit
) {
    clickFlow().onEach {
        delay(delayMillis)
        onClick(this)
    }.launchIn(lifecycle)
}

/**
 * 防止多次点击
 *
 * Example：
 *
 * view.clickTrigger(lifecycleScope) {
 *     showShortToast("公众号：ByteCode")
 * }
 */
var lastMillis: Long = 0

//@kotlin.internal.InlineOnly
inline fun View.clickTrigger(
    lifecycle: LifecycleCoroutineScope,
    intervalMillis: Long = 500,
    noinline onClick: (view: View) -> Unit
) {
    clickFlow().onEach {
        val curMillis = System.currentTimeMillis()
        if (curMillis - lastMillis < intervalMillis) {
            return@onEach
        }
        lastMillis = curMillis
        onClick(this)
    }.launchIn(lifecycle)
}