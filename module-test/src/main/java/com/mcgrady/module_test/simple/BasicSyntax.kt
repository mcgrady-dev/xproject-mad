package com.mcgrady.xproject

import com.mcgrady.module_test.simple.FromDecoder

/**
 * 基础语法
 * Created by mcgrady on 1/28/21.
 */

fun main(args: Array<String>) {

//    val length: Int? = getStrLength("xasdfkjliejlijgan;slejnfal;wejfliasjdflasdf")
//    println("string length = $length")

//    println(simpleWhen(1))
//    println(simpleWhen("Hello"))
//    println(simpleWhen(100000L))
//    println(simpleWhen(555F))
//    println(simpleWhen("XXX"))

//    simpleFruits()

    val fromDecoder = FromDecoder("recordId=123")
    println("recordId=${fromDecoder.get("recordId")}")
}

fun getStrLength(obj: Any): Int? = if(obj is String) obj.length else null

val items = listOf("aaa", "vvv", "eee", "rrtr", "fdfea", "tgtg")

fun simpleFor() {

    for (item in items) {
        println("item = $item length = ${getStrLength(item)}")
    }

    for (index in items.indices) {
        println("index = $index item = ${items[index]} length = ${getStrLength(items[index])}")
    }

    for (i in 1 until 100 step 3) {
        println("index = $i")
    }
}

fun simpleWhile() {

    var index = 0
    while (index < items.size) {
        println("item = ${items[index]} length = ${getStrLength(items[index])}")
        index++
    }
}

fun simpleRanges() {
    val x = 10
    val y = 9

    if (x in 1..y + 1) {
        println("fits in range.")
    }

    val tgtg = "tgtg"
    if (tgtg !in items.first()..items.last()) {
        println("$tgtg is out of items range")
    }

    val bbb = "bbb"
    if (bbb !in items.first()..items.last()) {
        println("$bbb is out of items range")
    }

    if (items.size !in items.indices) {
        println("list size is out of valid list indices range, too")
    }
}

fun simpleWhen(obj: Any): String =
        when (obj) {
            1   -> "One"
            "Hello" -> "Hi"
            is Long   -> "Long"
            !is String  -> "Not a string"
            else    -> "Unkown"
        }

fun simpleFruits() {
    items.filter { it.contains('a') }
            .sortedBy { it }
            .map { it.toUpperCase() }
            .forEach { println(it) }
}