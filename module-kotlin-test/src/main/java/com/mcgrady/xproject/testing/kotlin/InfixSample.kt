package com.mcgrady.xproject.testing.kotlin

/**
 * Created by mcgrady on 2021/12/23.
 */
fun main() {

    if ("Hello Kotlin" beginsWith "H") {
        println("Hello Kotlin startsWith H")
    }

    val listOf = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
    if (listOf has "Grape") {
        println("listOf has Grape")
    }

    mapOf("Apple" to "A")
}

infix fun String.beginsWith(prefix: String) = startsWith(prefix)

infix fun <T> Collection<T>.has(element: T) = contains(element)