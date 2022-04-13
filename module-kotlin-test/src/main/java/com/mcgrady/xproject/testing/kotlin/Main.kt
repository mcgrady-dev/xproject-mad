package com.mcgrady.xproject.testing.kotlin

fun main() {

//    val text: String? = " "
//
//    val name = text?.ifEmpty {
//        "xxx"
//    }
//
//    println("text is null or empty = ${text.isNullOrBlank()}")
//
//    val event = BaseEvent<String>()


//    val a: Int = 100
//    val boxedA: Int? = a
//    val anotherBoxedA: Int? = a
//
//    val b: Int = 10000
//    val boxedB: Int? = b
//    val anotherBoxedB: Int? = b
//
//    println(boxedA === anotherBoxedA) // true
//    println(boxedB === anotherBoxedB) // false

//    pascal().take(10).forEach(::println)

//    val list = listOf<Int>()
//    list.map { it * 2 }
//    println("list size ${list.size  }")
//    list.forEach {
//        println("index $it")
//    }

//    foo {
//        return@foo
//    }
}

inline fun foo(returning: () -> Unit) {
    println("before local return")
    returning()
    println("after local return")
}

fun pascal() = generateSequence(listOf(1)) { prev ->
    listOf(1) + (1..prev.lastIndex).map { prev[it - 1] + prev[it] } + listOf(1)
}