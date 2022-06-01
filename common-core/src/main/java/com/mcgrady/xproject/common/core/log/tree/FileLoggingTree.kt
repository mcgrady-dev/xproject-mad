/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject.common.core.log.tree

import android.R
import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * Created by mcgrady on 2021/12/31.
 */
class FileLoggingTree constructor(private val logCachePath: String) : Timber.Tree() {

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
