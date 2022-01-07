package com.mcgrady.xproject.common_core.log.tree

import android.R
import android.annotation.SuppressLint
import timber.log.Timber
import android.text.TextUtils
import android.util.Log

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException


/**
 * Created by mcgrady on 2021/12/31.
 */
class FileLoggingTree constructor(private val logCachePath: String): Timber.Tree() {

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (TextUtils.isEmpty(logCachePath)) {
            return
        }
        val file = File("$logCachePath/log.txt")
        Log.v(javaClass.simpleName, "file.path: ${file.absolutePath} ,message: ${R.id.message}")
        var writer: FileWriter?
        var bufferedWriter: BufferedWriter? = null
        try {
            writer = FileWriter(file)
            bufferedWriter = BufferedWriter(writer)
            bufferedWriter.write(R.id.message)
            bufferedWriter.flush()
        } catch (e: IOException) {
            Log.v(javaClass.simpleName, "存储文件失败")
            e.printStackTrace()
        } finally {
            bufferedWriter?.close()
        }
    }
}