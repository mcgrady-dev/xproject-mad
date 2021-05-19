package com.mcgrady.xproject.module_test.simple

/**
 * 方法和Lambda
 * Created by mcgrady on 2/9/21.
 */

fun main(args: Array<String>) {


}

fun testHightLevelFunction() {
    //高阶函数
    var list = listOf(1, 2, 3, 4, 5)
    list.forEach(::println)
    val newList = list.map {
        (it * 2).toString()
    }.forEach(::println)
    list.map(Int::toDouble).forEach(::println)

    val flatMapList = arrayOf(
        1..5,
        50..55
    )
    val mergeList = flatMapList.flatMap { intRange ->
        intRange.map { intElement ->
            "No.$intElement"
        }
    }

    mergeList.forEach {
        print("${it} ,")
    }
}

// 尾递归函数
fun testEndRecursion() {
    println(findFixPoint(0.212312312332))
}

//变长参数
fun testLongParam() {

    val a = arrayOf(1, 2, 3, 4)
    val asList = asList(*a, 5, 6, 7)
    for (i in asList) {
        println("it = $i")
    }
}

// 中辍符号
fun testInfix() {
    var enumCard = Rank.QUEEN of Suit.CLUBS
    println("rank = ${enumCard.rank} suit = ${enumCard.suit}")
}

//默认参数
fun testDefaultParam() {
    read(arrayOf(1, 2, 3, 4, 5), 1)
}

//命名参数
fun renameParam() {

    reformat(str = "kotlin",
        normalizeCase = false,
        upperCaseFirstLetter = false,
        divideByCamelHumps = true,
        wordSeparator = 'N'
    )
    reformat(str = "mcgrady")
}

tailrec fun findFixPoint(x: Double): Double {
    return if (x == Math.cos(x)) x else findFixPoint(Math.cos(x))
}

fun <T> asList(vararg ts: T): ArrayList<T> {
    val result = ArrayList<T>()
    for (t in ts) {
        result.add(t)
    }

    return result
}

fun reformat(
        str: String, normalizeCase: Boolean = true, upperCaseFirstLetter: Boolean = true,
        divideByCamelHumps: Boolean = false,
        wordSeparator: Char = ' ',
) {
    println("""
        str = $str
        normalCase = $normalizeCase
        upperCaseFirstLetter = $upperCaseFirstLetter
        divideByCamelHumps = $divideByCamelHumps
        wordSeparator = $wordSeparator
    """.trimIndent())
}

fun read(b: Array<Int>, off: Int = 0, len: Int = b.size) {
    println("b = ${b.toString()} off = $off len = $len")
}

//花形
enum class Suit {
    HEARTS,
    SPADES,
    CLUBS,
    DIAMONDS
}

data class Card(val rank: Rank, val suit: Suit)

//数字
enum class Rank {
    TWO, THREE, FOUR, FIVE,
    SIX, SEVEN, EIGHT, NINE,
    TEN, JACK, QUEEN, KING, ACE;

    infix fun of(suit: Suit) = Card(this, suit)
}