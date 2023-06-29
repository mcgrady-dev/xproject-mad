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