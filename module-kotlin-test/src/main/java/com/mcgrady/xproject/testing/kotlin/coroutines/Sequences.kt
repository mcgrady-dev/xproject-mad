package com.mcgrady.xproject.testing.kotlin.coroutines

/**
 * Created by mcgrady on 2022/1/21.
 */
fun main() {
    val words = "The quick brown fox jumps over the lazy dog".split(" ")
//    val lengthsList = words.filter { println("filter: $it"); it.length > 3 }
//        .map { println("length: ${it.length}"); it.length }
//        .take(4)
//    val wordsSequence = words.asSequence()
//    val lengthsSequence = wordsSequence.filter { println("filter: $it"); it.length > 3 }
//        .map { println("length: ${it.length}"); it.length }
//        .take(4)
//
//    println("Lengths of first 4 words longer than 3 chars:")
//    println(lengthsSequence.toList())

    val sequence = sequence {
        yield(1)
        yield(2)
        yield(3)
        yield(4)
        yieldAll(listOf(1,2,3,4))
    }

    for (element in sequence) {
        println("$element")
    }

}