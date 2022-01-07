package com.mcgrady.xproject

import android.content.Context
import android.net.*
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ThreadUtils
import com.mcgrady.xproject.common.core.SingleLiveData

/**
 * Created by mcgrady on 2021/8/9.
 */
class NetworkLiveData private constructor(context: Context): SingleLiveData<Int>() {

    companion object : SingletonHolder<NetworkLiveData, Context>(::NetworkLiveData)

    private val request: NetworkRequest = NetworkRequest.Builder().build()
    private val connectivityManager: ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object: ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            LogUtils.d("NetworkLiveData: onAvailable")
            sendMessage(NetworkState.CONNECT)
        }
        override fun onLost(network: Network) {
            super.onLost(network)
            LogUtils.d("NetworkLiveData: onLost")
            sendMessage(NetworkState.NONE)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            LogUtils.d("NetworkLiveData: onUnavailable")
            sendMessage(NetworkState.UNAVAILABLE)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            LogUtils.d("NetworkLiveData: onCapabilitiesChanged = $network $networkCapabilities")
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                sendMessage(NetworkState.WIFI)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                sendMessage(NetworkState.CELLULAR)
            }
        }
    }

    private fun sendMessage(state: Int) {
        ThreadUtils.runOnUiThreadDelayed({ this@NetworkLiveData.value = state }, 100)
    }

    override fun onActive() {
        super.onActive()
        LogUtils.d("NetworkLiveData: onActive")
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        LogUtils.d("NetworkLiveData: onInactive")
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}