package com.mcgrady.xproject.module_test.simple

import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by mcgrady on 5/1/21.
 */

fun main(args: Array<String>) {

    testSequence()
}

fun testOnlyIf() {
    val runnable = Runnable {
        println("Runnable::run")
    }

    val function: () -> Unit

    function = runnable::run

    onlyif(true, function)
}

fun testStringUtils() {
    println(StringUtilsFormJvmStatic.isEmpty(""))
    println(StringUtilsFormINSTANCE.isEmpty("xx"))
    println(StringUtilsFormCompObj.isEmpty(""))
}

inline fun onlyif(isDebug: Boolean, block: () -> Unit) {
    if (isDebug) block()
}

fun testDynamicProxy() {
    Zoo(Dog()).bark()
}

interface Animal {
    fun bark()
}

class Dog : Animal {
    override fun bark() {
        println(" Wo Wo Wo")
    }
}

class Zoo(animal: Animal): Animal by animal {

}

fun testSealedClass(superCommand: SuperCommand) = when(superCommand) {
    SuperCommand.A -> {
        println(superCommand)
    }

    SuperCommand.B -> {
        println(superCommand)
    }

    SuperCommand.C -> {
        println(superCommand)
    }

    SuperCommand.D -> {
        println(superCommand)
    }
}

sealed class SuperCommand {
    object A: SuperCommand()
    object B: SuperCommand()
    object C: SuperCommand()
    object D: SuperCommand()
}

fun testDec() {
    val user = AUser(18, "mcgrady")
    val (age, name) = user
    println(age)
    println(name)
}

data class AUser(var age: Int, var name: String) {

}

fun testFor() {
    repeat(10) {
        println(it)
    }

    val list = arrayListOf<String>("a", "b", "c", "d")
    for (str in list) {
        println(str)
    }

    for ((index: Int, str: String) in list.withIndex()) {
        println("第$index 个元素 $str")
    }
}

fun testMap() {
    val a: Array<String> = arrayOf("d", "e", "4", "5", "h", "8", "t")
    val index: Array<Int> = arrayOf(1, 5, 2, 4, 6, 3, 8, 7)

    index.filter {
        it < a.size
    }.map {
        a[it]
    }.reduce {a, index ->
        "$a$index"
    }.also {
        println("密码是：$it")
    }

    val items: List<Int> = listOf(1, 2, 3, 4, 5)
    items.convert {
        it + 1
    }.forEach {
        println(it)
    }
}

inline fun <T, E> Iterable<T>.convert(action: (T) -> E):Iterable<E> {
    val list: MutableList<E> = mutableListOf()
    for (item in this) list.add(action(item))
    return list
}

fun testScopeFunction() {
    val user = AUser(18, "mcgrady")

    val leftReuslt = user.let {user: AUser ->
        "let::${user.javaClass}"
    }
    println(leftReuslt)

    val runResult = user.run { "run::${this.javaClass}" }
    println(runResult)

    user.apply {
        println("age = $age name = $name")
    }

    user.also { user: AUser ->
        println("age = ${user.age} name = ${user.name}")
    }

    with(user) {
        this.age = 28
        this.name = ""
        println(this)
    }

    user.takeIf { it.name.length > 0 }?.also { println("姓名为 ${user.name}") } ?: println("姓名为空")
}

fun testSequence() {
    val sequence = sequenceOf(1, 2, 3, 4)
    val result: Sequence<Int> = sequence
        .map {
            println("Map $it")
            it * 2
        }.filter {
            println("Filter $it")
            it % 3 == 0
        }

    println(result.first())
}

val Float.dp
get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    Resources.getSystem().displayMetrics)

val RADIUS = 200f.dp