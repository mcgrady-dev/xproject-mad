package com.mcgrady.xproject.testing.kotlin.generic

/**
 * Created by mcgrady on 2021/12/22.
 */
fun main() {

    "mcgrady".build {

    }
}

class MyClass {
    fun <T : Number> method(param: T): T {
        return param
    }
}

fun <T> T.build(block: T.() -> Unit): T {
    block()
    return this
}