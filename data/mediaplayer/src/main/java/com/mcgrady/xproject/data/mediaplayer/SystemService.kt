package com.mcgrady.xproject.data.mediaplayer

import com.mcgrady.xproject.core.network.ApiResponse
import com.mcgrady.xproject.data.mediaplayer.model.subsonic.SubsonicResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface SystemService {

    @GET("ping")
    fun ping(@QueryMap params: Map<String, String>): ApiResponse<SubsonicResponse>

    @GET("getLicense")
    fun getLicense(@QueryMap params: Map<String, String>): ApiResponse<SubsonicResponse>
}