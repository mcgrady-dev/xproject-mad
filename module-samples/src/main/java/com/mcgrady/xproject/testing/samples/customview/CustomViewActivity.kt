package com.mcgrady.xproject.testing.samples.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mcgrady.xproject.testing.samples.R

class CustomViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)
    }
}