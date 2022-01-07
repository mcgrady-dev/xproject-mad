package com.mcgrady.xproject

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.LogUtils

/**
 * Created by mcgrady on 2021/10/27.
 */
open class AppLifecycleObserver : LifecycleObserver {

    companion object {
        val TAG = AppLifecycleObserver::class.simpleName
    }

    /**
     * ON_CREATE 在应用程序的整个生命周期中只会被调用一次
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        LogUtils.d("$TAG Lifecycle.Event.ON_CREATE")
    }

    /**
     * 应用程序出现到前台时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
        LogUtils.d("$TAG Lifecycle.Event.ON_START")
    }

    /**
     * 应用程序出现到前台时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
        LogUtils.d("$TAG Lifecycle.Event.ON_RESUME")
    }

    /**
     * 应用程序退出到后台时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
        LogUtils.d("$TAG Lifecycle.Event.ON_PAUSE")
    }

    /**
     * 应用程序退出到后台时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
        LogUtils.d("$TAG Lifecycle.Event.ON_STOP")
    }

    /**
     * 永远不会被调用到，系统不会分发调用ON_DESTROY事件
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        LogUtils.d("$TAG Lifecycle.Event.ON_DESTROY")
    }
}