package com.mcgrady.xproject.testing.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mcgrady.xproject.testing.test.R

class NormalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal)
    }
}