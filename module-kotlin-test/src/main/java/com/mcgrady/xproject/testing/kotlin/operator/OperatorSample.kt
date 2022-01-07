package com.mcgrady.xproject.testing.kotlin.operator

/**
 * Created by mcgrady on 2021/12/21.
 */
fun main() {
    val n = (1..20).random()
    println("$TAG: getLengthStr(str, n) = ${getLengthStr(NAME, n)}")
    println("$TAG: str.times(n) = ${NAME.times(n)}")
    println("$TAG: str * n = ${NAME * (n)}")
}

fun getLengthStr(str: String, n: Int): String {

    val builder = StringBuilder()
    repeat(n) {
        builder.append(str)
    }
    return builder.toString()
}

const val TAG = "OperatorSample"
const val NAME = "mcgrady|"