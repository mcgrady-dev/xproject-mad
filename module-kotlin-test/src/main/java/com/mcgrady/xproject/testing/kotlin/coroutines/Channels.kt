package com.mcgrady.xproject.testing.kotlin.coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by mcgrady on 2022/1/24.
 */
suspend fun main() {
    val channel = Channel<Int>()

    val producer = GlobalScope.launch {
        var i = 0
        while (true) {
            delay(100)
            channel.send(i++)
            println("send $i")
        }
    }

    val consumer = GlobalScope.launch {
        while (true) {
            val element = channel.receive()
            println("receive $element")
        }
    }

//    producer.join()
    consumer.join()
}