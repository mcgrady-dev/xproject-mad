package com.mcgrady.xproject.testing.samples

import com.mcgrady.xproject.common.core.app.BaseApplication
import com.mcgrady.xproject.common.core.log.Log
import com.mcgrady.xproject.common.core.utils.Utils

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        Log.d("is ART JVM ${Utils.isArtJvm()}")
    }
}