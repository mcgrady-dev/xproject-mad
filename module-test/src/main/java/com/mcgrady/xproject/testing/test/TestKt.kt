package com.mcgrady.xproject.testing.test

/**
 * Created by mcgrady on 2021/12/21.
 */

fun main() {
    println("randon string length = ${getRandomLengthStr("xdkjfakljeoajoe8jkdljalsdnnvmlasdjkfe")}")
}

fun getRandomLengthStr(str: String): String {
    val n = (1..20).random()
    val builder = StringBuilder()
    repeat(n) {
        builder.append(str)
    }
    return builder.toString()
}
