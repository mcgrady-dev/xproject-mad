package com.mcgrady.xproject.testing.samples

import com.mcgrady.xproject.common.core.app.BaseApplication
import com.mcgrady.xproject.common.core.log.Log
import com.mcgrady.xproject.common.core.utils.Utils
import kotlin.concurrent.thread

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        Log.d("is ART JVM ${Utils.isArtJvm()}")

        Log.d("""
            max memory = ${Runtime.getRuntime().maxMemory()}
            total memory = ${Runtime.getRuntime().totalMemory()}
            free memory = ${Runtime.getRuntime().freeMemory()}
            current used memory = ${Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()}
            
        """.trimIndent())

        thread {
            Thread.sleep(3000)
        }
    }
}