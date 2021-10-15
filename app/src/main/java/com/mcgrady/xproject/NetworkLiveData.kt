package com.mcgrady.xproject

import android.content.Context
import android.net.*
import androidx.lifecycle.LiveData

/**
 * Created by mcgrady on 2021/8/9.
 */
class NetworkLiveData private constructor(context: Context): LiveData<Int>() {

    companion object : SingletonHolder<NetworkLiveData, Context>(::NetworkLiveData)

    private val request: NetworkRequest = NetworkRequest.Builder().build()
    private val connectivityManager: ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object: ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(NetworkState.CONNECT)
        }
        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(NetworkState.NONE)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            postValue(NetworkState.UNAVAILABLE)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                postValue(NetworkState.WIFI)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                postValue(NetworkState.CELLULAR)
            }
        }
    }

    override fun onActive() {
        super.onActive()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}