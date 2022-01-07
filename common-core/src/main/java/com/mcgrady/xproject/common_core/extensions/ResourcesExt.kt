package com.mcgrady.xproject.common_core.extensions

import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.Utils

fun Int.asColor() = ContextCompat.getColor(Utils.getApp(), this)

fun Int.asDrawable() = ContextCompat.getDrawable(Utils.getApp(), this)