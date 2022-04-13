package com.mcgrady.xproject.testing.kotlin.thread

import com.mcgrady.xproject.testing.kotlin.flow.log
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * Created by mcgrady on 2021/12/23.
 */
fun main() {

    sampleSynchronized()
}

fun sampleSynchronized() {
    val room = Room()

//    val t1 = thread(name = "t1") {
//        for (i in 0..5000) {
//            room.increment()
//        }
//    }
    val t1 = Thread({
        for (i in 0..5000) {
            room.increment()
        }
    }, "t1")

//    val t2 = thread(name = "t2") {
//        for (i in 0..5000) {
//            room.decrement()
//        }
//    }

    val t2 = Thread({
        for (i in 0..5000) {
            room.decrement()
        }
    }, "t2")

    t1.start()
    t2.start()
    t1.join()
    t2.join()

    log("counter = ${room.counter}")
}

class Room {

    var counter: Int = 0
        private set

    @Synchronized
    fun increment() {
        counter++
//        counter.inc()
    }

    @Synchronized
    fun decrement() {
        counter--
//        counter.dec()
    }
}

fun sampleInterrupted() {
    val t1 = thread {
        log("enter sleep...")
        try {
//            Thread.sleep(2000)
            TimeUnit.SECONDS.sleep(2)
        } catch (e: InterruptedException) {
            log("wake up...")
            e.printStackTrace()
        }
    }

    Thread.sleep(1000)
    log("interrupt...")
    t1.interrupt()
}

fun sampleFutureTask() {
    val task = FutureTask<Int> {
        log("running...")
        Thread.sleep(1000)
        100
    }


    Thread(task).start()

    log("task.get = ${task.get()}")
}

fun sampleThreadLocal() {
    val local: ThreadLocal<Int> = object: ThreadLocal<Int>() {
        override fun initialValue(): Int {
            return 0
        }
    }

    val testRunnable = Runnable {
        for (i in 0..5) {
            Thread.sleep(1000)
            println("${Thread.currentThread().name}: local=${local.get()}")
            var value = local.get()
            local.set(++value)
        }
    }

    Thread(testRunnable).start()
    Thread.sleep(6000)
    Thread(testRunnable).start()
}