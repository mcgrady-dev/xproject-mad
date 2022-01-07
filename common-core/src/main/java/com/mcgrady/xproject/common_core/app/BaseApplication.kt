package com.mcgrady.xproject.common_core.app

import android.app.Application
import com.blankj.utilcode.util.ToastUtils
import com.mcgrady.xproject.common_core.extensions.asColor
import com.mcgrady.xproject.common_core.lifecycle.ActivityLifecycleCallbacksImpl
import com.mcgrady.xproject.common_core.log.Log

/**
 * Created by mcgrady on 2021/12/16.
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Log.init(this)

        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())

        ToastUtils.showShort("")
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}