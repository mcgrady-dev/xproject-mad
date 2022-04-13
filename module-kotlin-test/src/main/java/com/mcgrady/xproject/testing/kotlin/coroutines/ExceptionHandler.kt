package com.mcgrady.xproject.testing.kotlin.coroutines

import kotlinx.coroutines.*
import java.io.IOException
import kotlin.coroutines.CoroutineContext

/**
 * Created by mcgrady on 2022/1/20.
 */
suspend fun main() {

//    Thread.setDefaultUncaughtExceptionHandler { t: Thread, e: Throwable ->
//        println("Thread '${t.name}' throws an exception with message '${e.message}'")
//    }
//
//    throw ArithmeticException("Hey!")

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        log("${coroutineContext[CoroutineName]} Thread an exception with message: ${throwable.message}")
    }

////    log("1")
////    GlobalScope.launch(GlobalCoroutineExceptionHandler() + Dispatchers.Default) {
////        throw ArithmeticException("Hey!")
////    }.join()
////    log("2")
//
//
//    val job = GlobalScope.launch(exceptionHandler + CoroutineName("1")) {
//        log("Throwing exception from launch")
//        throw IndexOutOfBoundsException("Hey!!")
//    }

//    job.join()
//    log("Joined failed job")
//
//    val deferred = GlobalScope.async(exceptionHandler + CoroutineName("2")) {
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

//    val job = GlobalScope.launch(exceptionHandler + CoroutineName("①")) {
//        throw ArithmeticException("Hey!!")
//    }
//
//    try {
//        job.join()
//    } catch (e: ArithmeticException) {
//        log("Caught ArithmeticException form launch")
//    }
//
//    val deferred =
//        GlobalScope.async(exceptionHandler + CoroutineName("②")) {
//            throw ArithmeticException("Hey!!")
//        }
//
//    try {
//        deferred.await()
//    } catch (e: ArithmeticException) {
//        log("Caught ArithmeticException form async")
//    }

//    try {
//        GlobalScope.launch {
//            val job = launch(exceptionHandler + CoroutineName("①")) {
//                log("①")
//                throw ArithmeticException("Hey!!")
//            }
//
//            try {
//                log("① join")
//                job.join()
//            } catch (e: ArithmeticException) {
//                log("Caught ArithmeticException form launch")
//            }
//
//            val deferred =
//                async(exceptionHandler + CoroutineName("②")) {
//                    log("②")
//                    throw ArithmeticException("Hey!!")
//                }
//
//            try {
//                log("② await")
//                deferred.await()
//            } catch (e: ArithmeticException) {
//                log("Caught ArithmeticException form async")
//            }
//        }
//    } catch (e: ArithmeticException) {
//        log("Caught ArithmeticException form coroutineScope")
//    }
//
//    delay(100)


    val job = GlobalScope.launch(exceptionHandler) {
        launch {
            try {
                delay(Long.MAX_VALUE)
            } catch (e: Exception) {
                log("Caught Exception ${e.message}")
            } finally {
//                withContext(NonCancellable) {
//                    log("Children are cancelled, but exception is not handled until all children terminate")
//                    delay(100)
//                    log("The first child finished its non cancellable block")
//                }
                throw IOException()
            }
        }

        launch {
            delay(100)
            log("Second child throws an exception")
            throw ArithmeticException()
        }
        delay(Long.MAX_VALUE)
    }
    job.join()

}

//fun log(int: Int) = log(int.toString())

class GlobalCoroutineExceptionHandler : CoroutineExceptionHandler {

    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        println("Coroutine exception: $exception")
    }
}