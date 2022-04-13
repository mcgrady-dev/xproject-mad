package com.mcgrady.xproject.testing.kotlin

import kotlin.properties.Delegates

/**
 * Created by mcgrady on 2022/1/28.
 */
fun main() {
    val su = StockUpdate()
    val sd = StockDisplay()
    su.listeners.add(sd)
    su.price = 100
    su.price = 98
}

interface StockUpdateListener {
    fun onRise(price: Int)
    fun onFall(price: Int)
}

class StockDisplay: StockUpdateListener {
    override fun onRise(price: Int) {
        println("The latest stock price has risen to ${price}.")
    }

    override fun onFall(price: Int) {
        println("The latest stock price has fall to ${price}.")
    }
}

class StockUpdate {
    val listeners = mutableListOf<StockUpdateListener>()

    var price: Int by Delegates.observable(0) { _, old, new ->
        listeners.forEach {
            if (new > old) it.onRise(price) else it.onFall(price)
        }
    }
}