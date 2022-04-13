package com.mcgrady.xproject.testing.samples.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import com.mcgrady.xproject.common.core.log.Log
import com.mcgrady.xproject.testing.samples.R
import com.mcgrady.xproject.testing.samples.fragment.ui.main.SampleFragment

class SampleFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                val fragment = SampleFragment.newInstance()
                replace(R.id.container, fragment)
                setReorderingAllowed(true)
            }

//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, SampleFragment.newInstance())
//                .setReorderingAllowed(true)
//                .commitNow()
        }
    }
}