package com.mcgrady.xproject.testing.kotlin.hight_order_func

/**
 * Created by mcgrady on 2021/12/21.
 */
fun main() {

//    println("$TAG: plus = ${num1Andnum2(10, 10, ::plus)}")
//    println("$TAG: minus = ${num1Andnum2(10, 10, ::minus)}")
//    val plusResult = num1Andnum2(10, 10) { n1, n2 ->
//        n1 + n2
//    }
//    val minusResult = num1Andnum2(10, 10) { n1, n2 ->
//        n1 - n2
//    }
//    println("$TAG: lambda plus = $plusResult")
//    println("$TAG: lambda minus = $minusResult")
//
//    val builder = StringBuilder().build {
//
//    }

    val base: Base = Extended()
    base.run()
}

open class Base {

    open fun run() {
        println("Base run")
    }
}

class Extended : Base() {
    override fun run() {
        println("Extended run")
    }
}

fun num1Andnum2(num1: Int, num2: Int, operation: (Int, Int) -> Int): Int = operation(num1, num2)

fun plus(num1: Int, num2: Int): Int {
    return num1 + num2
}

fun minus(num1: Int, num2: Int): Int {
    return num1 - num2
}

const val TAG = "Height-Order-Function"

fun StringBuilder.build(block: StringBuilder.() -> Unit): StringBuilder {
    block()
    return this
}