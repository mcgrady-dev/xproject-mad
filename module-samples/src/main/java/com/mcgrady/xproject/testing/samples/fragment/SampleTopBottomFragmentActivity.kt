package com.mcgrady.xproject.testing.samples.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.mcgrady.xproject.testing.samples.R
import com.mcgrady.xproject.testing.samples.fragment.ui.main.OneFragment
import com.mcgrady.xproject.testing.samples.fragment.ui.main.TwoFragment

class SampleTopBottomFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity_top_bottom_fragment)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fl_container_top, OneFragment.newInstance("OneFragment"))
                add(R.id.fl_container_bottom, TwoFragment("TwoFragment"))
                setReorderingAllowed(true)
                addToBackStack("name")
            }
        }
    }
}