package com.mcgrady.xproject.core.network

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

object HttpLogging : HttpLoggingInterceptor.Logger {
    private const val TAG = "OkHttp"
    override fun log(message: String) {
        Timber.tag(TAG).d(message)
    }
}