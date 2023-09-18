package com.mcgrady.xproject

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class NiaBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
    BENCHMARK(".benchmark")
}
