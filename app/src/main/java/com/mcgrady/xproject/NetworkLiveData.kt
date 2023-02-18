/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.mcgrady.xarch.extension.runOnUiThread
import com.mcgrady.xarch.utils.SingleLiveData
import com.mcgrady.xarch.utils.SingletonHolder
import timber.log.Timber

/**
 * Created by mcgrady on 2021/8/9.
 */
class NetworkLiveData private constructor(private val context: Context) : SingleLiveData<Int>() {

    companion object : SingletonHolder<NetworkLiveData, Context>(::NetworkLiveData)

    private val request: NetworkRequest = NetworkRequest.Builder().build()
    private val connectivityManager: ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Timber.d("onAvailable")
            sendMessage(NetworkState.CONNECT)
        }
        override fun onLost(network: Network) {
            super.onLost(network)
            Timber.d("onLost")
            sendMessage(NetworkState.NONE)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Timber.d("onUnavailable")
            sendMessage(NetworkState.UNAVAILABLE)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            Timber.d("onCapabilitiesChanged = $network $networkCapabilities")
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                sendMessage(NetworkState.WIFI)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                sendMessage(NetworkState.CELLULAR)
            }
        }
    }

    private fun sendMessage(state: Int) {
        context.runOnUiThread { value = state }
//        ThreadUtils.runOnUiThreadDelayed({ this@NetworkLiveData.value = state }, 100)
    }

    override fun onActive() {
        super.onActive()
        Timber.d("onActive")
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        Timber.d("onInactive")
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
