package com.mcgrady.xproject.core.utils

import android.content.Context
import com.mcgrady.xproject.core.common.BuildConfig
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel


/**
 * 持久化数据
 * Created by mcgrady on 2021/10/21.
 */
object PreferenceUtil {

    private const val SP_MODE = 1
    private const val SP_KEY = "WBS_ANDROID"

    fun init(context: Context) {
        MMKV.initialize(context)
        MMKV.setLogLevel(if (BuildConfig.DEBUG) MMKVLogLevel.LevelInfo else MMKVLogLevel.LevelNone)
    }

    private val kv: MMKV by lazy {
        MMKV.defaultMMKV(SP_MODE, SP_KEY)
    }
    
    fun encode(key: String, value: String) {
        kv.encode(key, value)
    }

    fun encode(key: String, value: Int) {
        kv.encode(key, value)
    }

    fun encode(key: String, value: Long) {
        kv.encode(key, value)
    }
    
    fun encode(key: String, value: Boolean) {
        kv.encode(key, value)
    }

    fun encode(key: String, value: Float) {
        kv.encode(key, value)
    }

    fun decodeString(key: String): String {
        return decodeString(key, "")
    }

    fun decodeString(key: String, defaultValue: String): String {
        return kv.decodeString(key, defaultValue) ?: defaultValue
    }

    fun decodeInt(key: String): Int {
        return kv.decodeInt(key, 0)
    }

    fun decodeInt(key: String, defaultValue: Int): Int {
        return kv.decodeInt(key, defaultValue)
    }

    fun decodeLong(key: String): Long {
        return decodeLong(key, 0L)
    }

    fun decodeLong(key: String, defaultValue: Long): Long {
        return kv.decodeLong(key, defaultValue)
    }
    
    fun decodeBoolean(key: String, defaultValue: Boolean): Boolean {
        return kv.decodeBool(key, defaultValue)
    }

    fun decodeFloat(key: String): Float {
        return decodeFloat(key, 0F)
    }

    fun decodeFloat(key: String, defaultValue: Float): Float {
        return kv.decodeFloat(key, defaultValue)
    }

    // 移除某个key值已经对应的值
    fun remove(key: String) {
        kv.removeValueForKey(key)
    }

    // 查询某个key是否已经存在
    fun contain(key: String): Boolean? {
        return kv.containsKey(key)
    }

    // 返回所有的键值对
    fun getAll(): Map<String?, *>? {
        return kv.all
    }
}