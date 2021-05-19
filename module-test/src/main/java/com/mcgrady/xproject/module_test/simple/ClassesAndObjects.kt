package com.mcgrady.xproject.module_test.simple

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * 类和对象
 * Created by mcgrady on 2/2/21.
 */

fun main(args: Array<String>) {

//    val customer = Customer(120030L, "mcgrady", "18680249096")
//
//    val base = Base().apply {
//        f()
//    }
//
//    val a = A().apply {
//        f()
//    }
//
//    testSwap()
//
//    println("${Outer.Nester().javaClass.simpleName} foo() = ${Outer.Nester().foo()}")
//    println("${Outer().Inner().javaClass.simpleName} foo() = ${Outer().Inner().foo()}")

    //2021-02-09

    val ab = object : A1(1), B1 {
        override val y = 14
    }

    println("ab = ${ab.y}")

    val abHoc = object {
        var x = 1
        var y = 2
    }

    println("abHoc.x = ${abHoc.x} abHoc.y = ${abHoc.y}")

    val myclass = MyClass.create()
    println("${myclass.javaClass.simpleName}")

    val b = BaseImpl(100).apply {
        f()
    }
    Derived(b).f()

    val example = Example().apply {
        p = "New"
    }
    println(example.p)

    println(lazyValue)
    println(lazyValue)

    val user = User()
    user.name = "java"
    user.age = 24
    user.name = "kotlin"
    user.age = 17
    user.age = 100

    UserMap(mapOf(
            "name" to "mcgrady",
            "age" to 18
    )).apply {
        println("name = $name age = $age")
    }

    MutableUser(mutableMapOf(
            "name" to "Nicole",
            "age" to 18
    )).apply {
        println("name = $name age = $age")
    }
}

class User {
    var name: String by Delegates.observable("<no name>") { property, oldValue, newValue ->
        println("properts.name = ${property.name} oldValue = $oldValue newValue = $newValue")
    }

    var age: Int by Delegates.vetoable(18) { property, oldValue, newValue ->
        println("properts.name = ${property.name} oldValue = $oldValue newValue = $newValue")
        newValue > 18
    }
}

class UserMap(map: Map<String, Any?>) {
    val name by map
    val age: Int by map
}

class MutableUser(map: MutableMap<String, Any?>) {
    var name: String by map
    var age: Int by map

}

val lazyValue by lazy {
    println("computed!")
    "Hello Kotlin"
}

data class Customer(var id: Long, var name: String, var phone: String) {
    init {
        println("Customer initialized with value id $id name $name phone $phone")
        println("customer is empty ${isEmpty}")
    }

    var isEmpty: Boolean
        get() = when {
            id == 0L || name.isEmpty() -> true
            else -> false
        }
        private set(value) {
        }
}

open class Base {

    open val value: Int get() {
        return 10
    }

    open fun f() {
        println("${Base::class.simpleName} value = $value")
    }

    protected class Nested {
        val e: Int = 5
    }
}

interface IBase {

    val property: Int

    fun f() {
        println("${IBase::class.simpleName} property = $property")
    }
}

class BaseImpl(override val property: Int) : IBase {

    override fun f() {
        super.f()
    }
}

class Derived(b: IBase) : IBase by b {
    override val property: Int
        get() = 200

    override fun f() {
        super.f()
    }
}

class A : Base(), IBase {

    override val property: Int
        get() = 11

    override var value: Int = 20

    override fun f() {
        super<Base>.f()
        super<IBase>.f()
        println("${A::class.simpleName} value = $value")
    }
}

const val METHOD_DEPRECATED = "This method deprecated"

@Deprecated(METHOD_DEPRECATED)
fun deprecatedMethod() {

}

fun testSwap() {
    val mutableListOf = mutableListOf(1, 2, 3, 4, 5)
    for (i in mutableListOf) {
        println("befoer $i")
    }

    mutableListOf.add(7)
    mutableListOf.swap(2, 1)
    for (i in mutableListOf) {
        println("after $i")
    }

}

fun <T>MutableList<T>.swap(x: Int, y: Int) {
    val temp = this[x]
    this[x] = this[y]
    this[y] = temp
}

fun Any?.toString(): String {
    if (this == null) return "null"

    return toString()
}

fun doSomething(vararg numbers: Int) {

}

class Outer {

    private val bar = 1

    class Nester {

        fun foo() = 2
    }

    inner class Inner {
        fun foo() = bar
    }
}

open class A1(x: Int) {
    open val y = x
}

interface B1 {
}


//object ClassesAndObjects {
//
//    fun foo() {
//
//    }
//
//    val name: String
//    get() = "mcgrady"
//}

interface Factory<T> {

    fun create(): T
}

class MyClass {

    companion object : Factory<MyClass> {
        override fun create() = MyClass()
    }
}

class Example {
    var p: String by Delegate()
}

class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name} in $thisRef.'")
    }
}

