package com.mcgrady.xproject.data.mediaplayer.model.subsonic

import java.util.Date


data class License(
    val isValid: Boolean,
    val email: String? = null,
    val licenseExpires: Date? = null,
    val trialExpires: Date? = null
)