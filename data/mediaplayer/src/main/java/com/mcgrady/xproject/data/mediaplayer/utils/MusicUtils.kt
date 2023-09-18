package com.mcgrady.xproject.data.mediaplayer.utils

import android.os.Build
import android.text.Html
import android.text.Spanned


object MusicUtils {
    fun getReadableString(string: String?): String {
        return string?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT).toString()
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(it).toString()
            }
        } ?: ""
    }

    /*    fun getStreamUri(id: String?): Uri? {
            val params: Map<String, String?> = App.getSubsonicClientInstance(false).getParams()
            val uri = StringBuilder()
            uri.append(App.getSubsonicClientInstance(false).getUrl())
            uri.append("stream")
            if (params.containsKey("u") && params["u"] != null) uri.append("?u=").append(params["u"])
            if (params.containsKey("p") && params["p"] != null) uri.append("&p=").append(params["p"])
            if (params.containsKey("s") && params["s"] != null) uri.append("&s=").append(params["s"])
            if (params.containsKey("t") && params["t"] != null) uri.append("&t=").append(params["t"])
            if (params.containsKey("v") && params["v"] != null) uri.append("&v=").append(params["v"])
            if (params.containsKey("c") && params["c"] != null) uri.append("&c=").append(params["c"])
            if (!Preferences.isServerPrioritized()) uri.append("&maxBitRate=")
                .append(getBitratePreference())
            if (!Preferences.isServerPrioritized()) uri.append("&format=")
                .append(getTranscodingFormatPreference())
            uri.append("&id=").append(id)
            Log.d(TAG, "getStreamUri: $uri")
            return Uri.parse(uri.toString())
        }*/
}