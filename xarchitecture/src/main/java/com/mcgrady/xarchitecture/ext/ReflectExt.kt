package com.mcgrady.xarchitecture.ext

import android.view.LayoutInflater
import android.view.View

/**
 * Created by mcgrady on 2021/7/19.
 */

const val INFLATE_NAME = "inflate"
const val BIND_NAME = "bind"

fun <T> Class<T>.infateMethod() = getMethod(INFLATE_NAME, LayoutInflater::class.java)

fun <T> Class<T>.bindMethod() = getMethod(BIND_NAME, View::class.java)