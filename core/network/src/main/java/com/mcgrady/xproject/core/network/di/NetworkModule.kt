package com.mcgrady.xproject.core.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.mcgrady.xproject.core.base.BaseApplication
import com.mcgrady.xproject.core.common.BuildConfig
import com.mcgrady.xproject.core.network.HttpLogging
import com.mcgrady.xproject.core.network.interceptors.CacheControlInterceptor
import com.mcgrady.xproject.core.network.interceptors.ForceCacheInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.logging.LoggingEventListener
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {

        val collector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .collector(collector)
            .maxContentLength(250_000L)
            .redactHeaders(emptySet())
            .skipPaths("anything")
            .alwaysReadResponseBody(false)
            .build()

        return with(OkHttpClient.Builder()) {
            addInterceptor(chuckerInterceptor)
            if (BuildConfig.DEBUG) {
                eventListenerFactory(LoggingEventListener.Factory(HttpLogging))
            }
            addInterceptor(
                HttpLoggingInterceptor(HttpLogging).apply {
                    level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
                }
            )
            val httpCacheDir = File(BaseApplication.instance.cacheDir, "http-cache")
            val cacheSize: Long = 10 * 1024 * 1024 // 10Mib
            cache(Cache(httpCacheDir, cacheSize))
            addNetworkInterceptor(CacheControlInterceptor())
            addInterceptor(ForceCacheInterceptor())

            //暴力方法：信任所有SSL证书
//            connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
//            sslSocketFactory(TrustAll.socketFactory(), TrustAll.trustManager())
//            hostnameVerifier(TrustAll.hostnameVerifier())

            build()
        }
    }
}