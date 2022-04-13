package com.mcgrady.xproject.testing.kotlin.reflex

import java.util.*

/**
 * Created by mcgrady on 2022/1/27.
 */
fun main() {
    val cacheAnnotion = Hero::class.annotations.find { it is Cache } as Cache?
    println("namspace ${cacheAnnotion?.namespace}")
    println("expires ${cacheAnnotion?.expires}")
}

annotation class Cache(val namespace: String, val expires: Int)
annotation class CacheKey(val keyName: String, val buckets: IntArray)

@Cache(namespace = "hero", expires = 3600)
data class Hero(
    @CacheKey(keyName = "heroName", buckets = intArrayOf(1,2,3))
    val name: String,
    val attack: Int,
    val defense: Int,
    val initHp: Int
)
