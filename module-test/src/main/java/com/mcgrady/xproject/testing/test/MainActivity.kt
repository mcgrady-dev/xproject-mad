package com.mcgrady.xproject.testing.test

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.italic
import androidx.core.view.updatePadding
import com.mcgrady.xproject.common.core.extensions.systemService
import com.mcgrady.xproject.testing.test.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textview = findViewById<TextView>(R.id.tv_text)

        textview.text = buildSpannedString {
            append("hello world")
            appendLine()
            bold {
                append("go kotlin")
                appendLine()
                italic { append("double sized quote text") }
            }
        }

        textview.updatePadding(left = 100)

//        getSharedPreferences("xx", Context.MODE_PRIVATE).edit(true) {
//
//        }
//
//        contentValuesOf()
//
//        testKotlin()

    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        //release
    }

    private fun testKotlin() {
        val a: Int = 100
        val boxedA: Int? = a
        val anotherBoxedA: Int? = a

        val b: Int = 10000
        val boxedB: Int? = b
        val anotherBoxedB: Int? = b

        println(boxedA === anotherBoxedA) // true
        println(boxedB === anotherBoxedB) // false

        Handler(Looper.getMainLooper()).postDelayed(10) {

        }

        runOnUiThread {  }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getSystemService(context: Context) {

        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val alarmManager2: AlarmManager = systemService()
    }
}