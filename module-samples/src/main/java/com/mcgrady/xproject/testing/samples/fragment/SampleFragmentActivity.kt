package com.mcgrady.xproject.testing.samples.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mcgrady.xproject.testing.samples.fragment.ui.main.SampleFragment

class SampleFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SampleFragment.newInstance())
                .commitNow()
        }
    }
}