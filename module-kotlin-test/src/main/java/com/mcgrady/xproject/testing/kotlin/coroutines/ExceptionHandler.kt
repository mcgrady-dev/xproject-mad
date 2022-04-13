package com.mcgrady.xproject.testing.kotlin.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by mcgrady on 2022/1/20.
 */
fun main() = runBlocking {

//    Thread.setDefaultUncaughtExceptionHandler { t: Thread, e: Throwable ->
//        println("Thread '${t.name}' throws an exception with message '${e.message}'")
//    }
//
//    throw ArithmeticException("Hey!")

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        log("${coroutineContext[CoroutineName]} Thread an exception with message: ${throwable.message}")
    }

//    log("1")
//    GlobalScope.launch(GlobalCoroutineExceptionHandler() + Dispatchers.Default) {
//        throw ArithmeticException("Hey!")
//    }.join()
//    log("2")


//    val job = GlobalScope.launch {
//        log("Throwing exception from launch")
//        throw IndexOutOfBoundsException()
//    }
//
//    job.join()
//    log("Joined failed job")
//
//    val deferred = GlobalScope.async {
//        log("Throwing exception from async")
//        throw ArithmeticException()
//    }
//    try {
//        deferred.await()
//        log("Unreached")
//    } catch (e: ArithmeticException) {
//        log("Caught ArithmeticException")
//    }

//    log(1)
//    try {
//        supervisorScope { // ①
//            log(2)
//            launch(exceptionHandler + CoroutineName("②")) { // ②
//                log(3)
//                launch(exceptionHandler + CoroutineName("③")) { // ③
//                    log(4)
//                    delay(100)
//                    throw ArithmeticException("Hey!!")
//                }
//                log(5)
//            }
//            log(6)
//            val job = launch { // ④
//                log(7)
//                delay(1000)
//            }
//            try {
//                log(8)
//                job.join()
//                log("9")
//            } catch (e: Exception) {
//                log("10. $e")
//            }
//        }
//        log(11)
//    } catch (e: Exception) {
//        log("12. $e")
//    }
//    log(13)

    launch(exceptionHandler + Dispatchers.Default) {
        throw ArithmeticException("Hey!!")
    }
}

fun log(int: Int) = log(int.toString())

class GlobalCoroutineExceptionHandler: CoroutineExceptionHandler {

    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        println("Coroutine exception: $exception")
    }
}