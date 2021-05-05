package com.mcgrady.module_test.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

/**
 * Created by mcgrady on 1/29/21.
 */
class MyFragmentFactory: FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =
            when(className) {
                MyFragment::class.java.name-> MyFragment()
                else-> super.instantiate(classLoader, className)
            }
}