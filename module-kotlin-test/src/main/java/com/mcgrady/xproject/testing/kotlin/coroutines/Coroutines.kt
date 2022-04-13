package com.mcgrady.xproject.testing.kotlin.coroutines

import kotlinx.coroutines.*
import kotlin.concurrent.thread

/**
 * Created by mcgrady on 2022/1/14.
 */
fun main() = runBlocking {
//    doWork()
//    log("Done")

    testCoroutineContext()
}

fun testCoroutineContext() {
    val coroutineContext1 = Job() + CoroutineName("这是第一个上下文")
    log("coroutineContext1 $coroutineContext1")
    val coroutineContext2 = coroutineContext1 + Dispatchers.Default + CoroutineName("这是第二个上下文")
    log("coroutineContext2 $coroutineContext2")
    val coroutineContext3 = coroutineContext2 + Dispatchers.Main + CoroutineName("这是第三个上下文")
    log("coroutineContext3 $coroutineContext3")
}

suspend fun doWork() = coroutineScope {

    thread { }

    launch {
        delay(100L)
        log("World!")
    }

    log("Hello")
}

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")