package com.mcgrady.xproject.common.widget.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * Created by mcgrady on 2022/1/4.
 */
fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.remove() {
    this.visibility = View.GONE
}

fun ViewGroup.inflate(@LayoutRes viewType: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(viewType, this, attachToRoot)