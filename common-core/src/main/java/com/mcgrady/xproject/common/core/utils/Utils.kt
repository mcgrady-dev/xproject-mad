package com.mcgrady.xproject.common.core.utils

/**
 * Created by mcgrady on 2022/3/2.
 */
object Utils {

    fun isArtJvm(): Boolean {
        val version = System.getProperty("java.vm.version")
        return version.firstNotNullOf {
            return it.digitToInt() > 1
        }
    }

    fun currentUsedMemory() = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
}