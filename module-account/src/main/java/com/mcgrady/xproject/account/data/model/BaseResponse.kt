package com.mcgrady.xproject.account.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class BaseResponse<T>(val code: Int, val message: String, val data: T)