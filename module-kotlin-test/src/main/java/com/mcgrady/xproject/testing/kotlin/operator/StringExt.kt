package com.mcgrady.xproject.testing.kotlin.operator

/**
 * Created by mcgrady on 2021/12/21.
 */
operator fun String.times(n: Int): String = StringBuilder().run {
    repeat(n) {
        append(this@times)
    }
    toString()
}

