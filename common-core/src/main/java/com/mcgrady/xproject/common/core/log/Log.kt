package com.mcgrady.xproject.common.core.log

import android.content.Context
import com.blankj.utilcode.util.PathUtils
import com.mcgrady.xproject.common.core.BuildConfig
import com.mcgrady.xproject.common.core.log.tree.FileLoggingTree
import com.mcgrady.xproject.common.core.log.tree.ReleaseTree
import org.jetbrains.annotations.NonNls
import timber.log.Timber

/**
 * Created by mcgrady on 2021/12/21.
 */
object Log {

    @JvmStatic
    fun init(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
            Timber.plant(FileLoggingTree(PathUtils.getInternalAppCachePath()))
        }
    }

    /** Log a verbose message with optional format args. */
    @JvmStatic
    fun v(@NonNls message: String?, vararg args: Any?) {
        Timber.v(message, args)
    }

    /** Log a verbose exception and a message with optional format args. */
    @JvmStatic
    fun v(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.v(t, message, args)
    }

    /** Log a verbose exception. */
    @JvmStatic
    fun v(t: Throwable?) {
        Timber.v(t)
    }

    /** Log a debug message with optional format args. */
    @JvmStatic
    fun d(@NonNls message: String?, vararg args: Any?) {
        Timber.d(message, args)
    }

    /** Log a debug exception and a message with optional format args. */
    @JvmStatic
    fun d(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.d(t, message, args)
    }

    /** Log a debug exception. */
    @JvmStatic
    fun d(t: Throwable?) {
        Timber.d(t)
    }

    /** Log an info message with optional format args. */
    @JvmStatic
    fun i(@NonNls message: String?, vararg args: Any?) {
        Timber.i(message, args)
    }

    /** Log an info exception and a message with optional format args. */
    @JvmStatic
    fun i(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.i(t, message, args)
    }

    /** Log an info exception. */
    @JvmStatic
    fun i(t: Throwable?) {
        Timber.i(t)
    }

    /** Log a warning message with optional format args. */
    @JvmStatic
    fun w(@NonNls message: String?, vararg args: Any?) {
        Timber.w(message, args)
    }

    /** Log a warning exception and a message with optional format args. */
    @JvmStatic
    fun w(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.w(t, message, args)
    }

    /** Log a warning exception. */
    @JvmStatic
    fun w(t: Throwable?) {
        Timber.w(t)
    }

    /** Log an error message with optional format args. */
    @JvmStatic
    fun e(@NonNls message: String?, vararg args: Any?) {
        Timber.e(message, args)
    }

    /** Log an error exception and a message with optional format args. */
    @JvmStatic
    fun e(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.e(t, message, args)
    }

    /** Log an error exception. */
    @JvmStatic
    fun e(t: Throwable?) {
        Timber.e(t)
    }

    /** Log an assert message with optional format args. */
    @JvmStatic
    fun wtf(@NonNls message: String?, vararg args: Any?) {
        Timber.wtf(message, args)
    }

    /** Log an assert exception and a message with optional format args. */
    @JvmStatic
    fun wtf(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        Timber.wtf(t, message, args)
    }

    /** Log an assert exception. */
    @JvmStatic
    fun wtf(t: Throwable?) {
        Timber.wtf(t)
    }
}