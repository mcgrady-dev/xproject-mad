package com.mcgrady.xproject.testing.kotlin.collection

/**
 * Created by mcgrady on 2022/1/27.
 */

fun main() {

    val list = listOf(1, 2, 3, 4)
    var newList = list.mapTo(mutableListOf()) {
        it * 2
    }

    newList.forEach {
        println("it $it")
    }
}

inline fun <T, R, C: MutableCollection<in R>> Iterable<T>.mapTo(destination: C, transform: (T) -> R): C {
    for (item in this) {
        destination.add(transform(item))
    }

    return destination
}