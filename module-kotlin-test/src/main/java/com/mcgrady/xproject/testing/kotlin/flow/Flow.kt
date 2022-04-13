package com.mcgrady.xproject.testing.kotlin.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*
import java.lang.Exception
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

/**
 * Created by mcgrady on 2022/1/14.
 */
fun main() = runBlocking<Unit> {

    // suspend
//    simple().forEach { value ->
//        println(value)
//    }

    // check blocked
//    launch {
//        for (k in 1..3) {
//            println("I'm not blocked $k")
//            delay(300L)
//        }
//    }
//    simpleFlow().collect { value -> log("Collected $value") }

    // cold flow
//    println("Calling simple function...")
//    val flow = simpleFlow()
//    println("Calling collect...")
//    flow.collect { value -> println(value) }
//    println("Calling collect again...")
//    flow.collect { value -> println(value) }

    // close flow
//    withTimeoutOrNull(250) {
//        simpleFlow().collect { value -> println(value) }
//    }
//    println("Done")

    // flowOf
//    flowOf(1, 2, 3).collect { value ->
//        delay(1000L)
//        println(value)
//    }

    // asFlow
//    (1..3).asFlow().collect { value ->
//        delay(1000L)
//        println(value)
//    }

    // map
//    (1..3).asFlow()
//        .map { request ->  performRequest(request) }
//        .collect { value -> println(value) }

    //transform
//    simpleFlow()
//        .transform { request ->
//            emit("Making request $request")
//            emit(performRequest(request))
//        }
//        .collect { response ->
//            println(response)
//        }

    // take
//    numbers()
//        .take(2)
//        .collect { value -> println(value) }

    // 末端操作流 reduce
//    val sum = (1..5).asFlow()
//        .map {
//            println(it)
//            it * it
//        }
//        .reduce { a, b ->
//            println("a = $a b = $b")
//            a + b
//        }
//    println(sum)

    // 连续的流
//    (1..5).asFlow()
//        .filter { value ->
//        println("$value % 2 = ${value % 2} filter is ${value % 2 == 0}")
//            value % 2 == 0
//        }
//        .map { value ->
//            "String $value"
//        }.collect { response ->
//            println("response $response")
//        }

    // witContext
//    simpleWitContext().collect { value -> log("Collected $value") }

    //flowOn
//    simpleFlowOn()
//        .buffer()
//        .collect { value ->
//            delay(300L)
//            log("Collected $value")
//        }

    //catch
//    simpleCatch().catch { e ->
////        e.printStackTrace()
//        emit("Caught $e")
//    }.collect { value -> log("Collected $value") }

    //time
//    simpleTime()

    //withTimeout
//    simpleWithTimeout()

    //buffer
//    val time = measureTimeMillis {
//        simpleFlow()
//            .buffer()
//            .collect { value ->
//                delay(300L)
//                log("Collected $value")
//            }
//    }
//    log("Collected in $time ms")

    //conflate
//    val time = measureTimeMillis {
//        simpleFlow()
//            .conflate()
//            .collect { value ->
//                delay(300L)
//                log("Collected $value")
//            }
//    }
//    log("Collected in $time ms")

    // 合并操作
//    simpleFlow()
//        .conflate()
//        .collect { value ->
////            delay(300L)
//            log("Collected $value")
//        }

    // 处理最新值
//    simpleFlow()
//        .collectLatest { value ->
//            delay(1L)
//            log("Collected $value")
//        }

    // 合并操作 zip combine
//    val nums = (1..3).asFlow().onEach { delay(300) }
//    val strs = flowOf("one", "two", "three").onEach { delay(400) }
//    val startTime = System.currentTimeMillis()
//    nums.combine(strs) { a, b ->
//        "$a -> $b"
//    }.collect { value ->
//        log("$value at ${System.currentTimeMillis() - startTime} ms from start")
//    }

    // 展平流
//    val startTime = System.currentTimeMillis()
//    (1..3).asFlow().onEach { delay(100) }.flatMapLatest { requestFlow(it) }.collect { value ->
//        println("$value at ${System.currentTimeMillis() - startTime} ms from start")
//    }

    // try catch
//    simpleTryCatch().onEach { value ->
//        check(value <= 1) { "Collected $value" }
//        println(value)
//    }.map { value ->
//        "string $value"
//    }.catch { e ->
//        println(e)
//        emit("error")
//    }.onCompletion { cause -> println("Flow completed with $cause") }.collect { value ->
//        println(value)
//    }

    // channel
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) {
            log("send ${x * x}")
            channel.send(x * x)
        }
        channel.close()
    }
//    repeat(5) { log("receive ${channel.receive()}") }
    for (y in channel) log("channel $y")
    log("Done")

    // 管道
//    val numbers = produceNumbers()
//    val squares = square(numbers)
//
//    repeat(5) {
//        log("receive ${squares.receive()}")
//    }
//    log("Done")
//    coroutineContext.cancelChildren()

//    var cur = produceNumbers(2)
//    repeat(10) {
//        val prime = cur.receive()
//        log("prime $prime")
//        cur = filter(cur, prime)
//    }
//    coroutineContext.cancelChildren() // 取消所有的子协程来让主协程结束

    // 扇出

}

fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce {
    for (x in numbers) if (x % prime != 0) send(x)
}

fun CoroutineScope.produceNumbers(start: Int = 1) = produce {
    var x = start
    while (true) send(x++)
}

fun CoroutineScope.square(numbers: ReceiveChannel<Int>) : ReceiveChannel<Int> = produce {
    for (x in numbers) send(x * x)
}

fun simpleTryCatch(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i) // 发射下一个值
    }
}


fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500)
    emit("$i: Second")
}

suspend fun simpleWithTimeout() {

    val flow = flow {
        for (i in 1..3) {
            delay(500L)
            log("emit $i")
            emit(i)
        }
    }

    withTimeoutOrNull(1600L) {
        flow.collect {
            delay(500L)
            log("consume $it")
        }
    }
}

suspend fun simpleTime() {
    val time = measureTimeMillis {
        simpleFlow()
            .collect { value ->
            println("Collected $value")
        }
    }
    log("time $time")
}

fun simpleCatch(): Flow<String> = flow {
    for (i in 1..3) {
        println("Emiting $i")
        emit(i)
    }
}.map { value ->
    check( value <= 1) { "Crashed on $value" }
    "string $value"
}

fun simpleFlowOn(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100L)
        log("Emiting $i")
        emit(i)
    }
}.flowOn(Dispatchers.Default)

fun simpleWitContext(): Flow<Int> = flow {
    withContext(Dispatchers.Default) {
        for (i in 1..3) {
            delay(100L)
            log("Emiting $i")
            emit(i)
        }
    }
}

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

//sampleStart
fun numbers(): Flow<Int> = flow {
    try {
        emit(1)
        emit(2)
        println("This line will not execute")
        emit(3)
    } catch (e: Exception) {
        e.printStackTrace()
        println("Exception in numbers")
    } finally {
        println("Finally in numbers")
    }
}

suspend fun performRequest(request: Int): String {
    delay(100L)
    return "response $request"
}

suspend fun simple(): List<Int> {
    delay(100L)
    return listOf(1, 2, 3)
}

fun simpleFlow(): Flow<Int> = flow {
    log("Started simple flow")
    for (i in 1..3) {
        delay(300L)
//        Thread.sleep(100L)
        log("Emitting $i")
        emit(i)
    }
}