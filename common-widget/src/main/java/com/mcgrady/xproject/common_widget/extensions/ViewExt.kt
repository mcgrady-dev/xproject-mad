package com.mcgrady.xproject.common_widget.extensions

import android.view.View

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