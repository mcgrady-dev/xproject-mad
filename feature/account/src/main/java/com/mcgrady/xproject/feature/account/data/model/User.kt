package com.mcgrady.xproject.feature.account.data.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = false)
data class User(val id: Long, val name: String, val age: Int, val email: String, val avatar: String)
