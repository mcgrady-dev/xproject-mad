package com.mcgrady.xproject.common.core.network

import com.mcgrady.xproject.common.core.network.operators.SandwichOperator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okio.Timeout

object NetworkInitializer {


    @JvmStatic
    var successCodeRange: IntRange = 200..299

    @JvmStatic
    var sandwichOperators: MutableList<SandwichOperator> = mutableListOf()

    @JvmSynthetic
    var sandwichScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    @JvmStatic
    public var sandwichTimeout: Timeout? = null
}