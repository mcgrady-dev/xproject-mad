package com.mcgrady.xproject.core.extensions

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

@Suppress("UNCHECKED_CAST")
fun <T> AppCompatActivity.whichFragment(@IdRes id: Int): T {
    return supportFragmentManager.findFragmentById(id) as T
}