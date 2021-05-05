package com.mcgrady.module_test.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.savedstate.SavedStateRegistry
import com.mcgrady.module_test.R
import com.mcgrady.module_test.fragment.MyFragment
import com.mcgrady.module_test.fragment.MyFragmentFactory
import com.mcgrady.module_test.lifecycle.FragmentLifecycle

/**
 * Created by mcgrady on 1/29/21.
 */
class MyActivity: AppCompatActivity(R.layout.activity_my) {

    private var fragmentLifecycle = FragmentLifecycle()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = MyFragmentFactory()
        super.onCreate(savedInstanceState)
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycle, true)

        setTitle(javaClass.simpleName)
        supportFragmentManager
            .commit {
                setReorderingAllowed(true)
                add<MyFragment>(R.id.container)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycle)
    }
    
    fun testFragment() {

    }
}