package com.mcgrady.xproject.testing.kotlin.coroutines

import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.*

/**
 * Created by mcgrady on 2022/1/14.
 */
fun main() {
//    doWork()
//    log("Done")

//    testCoroutineContext()

//    GlobalScope.launch {
//        log("launch ${Job.currentJob()}")
//    }

//    log("runBlocking ${Job.currentJob()}")

//    GlobalScope.launch {
//        log("1")
//        val job = async {
//            log("2")
//            delay(1000)
//            log("3")
//            "Hello"
//        }
//        log("4")
//        val result = job.await()
//        log("5 $result")
//    }.join()
//    log("6")

//    Thread.setDefaultUncaughtExceptionHandler { t: Thread, e: Throwable ->
//        println("Thread '${t.name}' throws an exception with message '${e.message}'")
//    }

//    val thread = thread {
//        try {
//            Thread.sleep(2000)
//        } catch (e: InterruptedException) {
//            log("Caught InterruptedException")
//        }
//    }
//    thread.interrupt()

//    val job = launch {
//        log(1)
//        try {
//            delay(1000)
//        } catch (e: Exception) {
//            log("Caught Exception ${e.message}")
//        }
//        log(2)
//    }
//    delay(100)
//    log(3)
//    job.cancel()
//    log(4)

//    val continuation = suspend {
//        log("In Coroutine.")
//        5
//    }.createCoroutine(object: Continuation<Int> {
//        override val context: CoroutineContext
//            get() = EmptyCoroutineContext
//
//        override fun resumeWith(result: Result<Int>) {
//            log("Coroutine End: $result")
//        }
//    })
//    continuation.resume(Unit)

//    suspend {
//        log("In Coroutine.")
//        delay(100)
//        5
//    }.startCoroutine(object: Continuation<Int> {
//        override val context: CoroutineContext
//            get() = EmptyCoroutineContext
//
//        override fun resumeWith(result: Result<Int>) {
//            log("Coroutine End: $result")
//        }
//    })
}

suspend inline fun Job.Key.currentJob() = coroutineContext[Job]

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

//fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
fun log(any: Any) = println("[${Thread.currentThread().name}] $any")