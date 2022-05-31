@file:Suppress("NOTHING_TO_INLINE")

package com.mcgrady.xarchitecture.ext

import android.text.Editable
import android.widget.EditText
import androidx.annotation.CheckResult
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

/**
 * Created by mcgrady on 2022/5/31.
 */

@CheckResult
//@kotlin.internal.InlineOnly
inline fun EditText.doAfterTextChangeFlow(): Flow<Editable?> = callbackFlow {
    val textChangedListener = doAfterTextChanged {
        safeOffer(it)
    }
    awaitClose {
        removeTextChangedListener(textChangedListener)
    }
}

@CheckResult
//@kotlin.internal.InlineOnly
inline fun EditText.doBeforeTextChangeFlow(): Flow<CharSequence?> = callbackFlow {
    val textChangeListener = doBeforeTextChanged { text, _, _, _ ->
        safeOffer(text)
    }
    awaitClose { removeTextChangedListener(textChangeListener) }
}

@CheckResult
//@kotlin.internal.InlineOnly
inline fun EditText.doTextChangedFlow(): Flow<CharSequence?> = callbackFlow {
    val textChangeListener = doOnTextChanged { text, _, _, _ ->
        safeOffer(text)
    }
    awaitClose { removeTextChangedListener(textChangeListener) }
}

inline fun EditText.textChange(
    lifecycle: LifecycleCoroutineScope,
    crossinline onChange: (s: String) -> Unit
) {
    doAfterTextChangeFlow()
        .onEach {
            onChange(it.toString())
        }.launchIn(lifecycle)
}

inline fun EditText.textChange(
    lifecycle: LifecycleCoroutineScope,
    timeoutMillis: Long = 500,
    crossinline onChange: (s: String) -> Unit
) {
    doAfterTextChangeFlow()
        .debounce(timeoutMillis)
        .onEach {
            onChange(it.toString())
        }.launchIn(lifecycle)
}

inline fun EditText.textChangeBefore(
    lifecycle: LifecycleCoroutineScope,
    crossinline onChange: (s: String) -> Unit
) {
    doBeforeTextChangeFlow()
        .onEach {
            onChange(it.toString())
        }.launchIn(lifecycle)
}

inline fun EditText.textChangeBefore(
    lifecycle: LifecycleCoroutineScope,
    timeoutMillis: Long = 500,
    crossinline onChange: (s: String) -> Unit
) {
    doBeforeTextChangeFlow()
        .debounce(timeoutMillis)
        .onEach {
            onChange(it.toString())
        }.launchIn(lifecycle)
}

inline fun EditText.textChangeAfter(
    lifecycle: LifecycleCoroutineScope,
    crossinline onChange: (s: String) -> Unit
) {
    doAfterTextChangeFlow()
        .onEach {
            onChange(it.toString())
        }.launchIn(lifecycle)
}

inline fun EditText.textChangeAfter(
    lifecycle: LifecycleCoroutineScope,
    timeoutMillis: Long = 500,
    crossinline onChange: (s: String) -> Unit
) {
    doAfterTextChangeFlow()
        .debounce(timeoutMillis)
        .onEach {
            onChange(it.toString())
        }.launchIn(lifecycle)
}