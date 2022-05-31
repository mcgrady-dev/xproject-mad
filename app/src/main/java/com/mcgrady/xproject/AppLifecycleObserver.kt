package com.mcgrady.xproject

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import timber.log.Timber

/**
 * Created by mcgrady on 2021/10/27.
 */
open class AppLifecycleObserver : DefaultLifecycleObserver {

    companion object {
        val TAG = AppLifecycleObserver::class.simpleName
    }

    /**
     * ON_CREATE 在应用程序的整个生命周期中只会被调用一次
     */
    override fun onCreate(owner: LifecycleOwner) {
//        Timber.d("$TAG Lifecycle.Event.ON_CREATE")
        Timber.d("$TAG Lifecycle.Event.ON_CREATE")
    }

    /**
     * 应用程序出现到前台时调用
     */
    override fun onStart(owner: LifecycleOwner) {
        Timber.d("$TAG Lifecycle.Event.ON_START")
    }

    /**
     * 应用程序出现到前台时调用
     */
    override fun onResume(owner: LifecycleOwner) {
        Timber.d("$TAG Lifecycle.Event.ON_RESUME")
    }

    /**
     * 应用程序退出到后台时调用
     */
    override fun onPause(owner: LifecycleOwner) {
        Timber.d("$TAG Lifecycle.Event.ON_PAUSE")
    }

    /**
     * 应用程序退出到后台时调用
     */
    override fun onStop(owner: LifecycleOwner) {
        Timber.d("$TAG Lifecycle.Event.ON_STOP")
    }

    /**
     * 永远不会被调用到，系统不会分发调用ON_DESTROY事件
     */
    override fun onDestroy(owner: LifecycleOwner) {
        Timber.d("$TAG Lifecycle.Event.ON_DESTROY")
    }
}