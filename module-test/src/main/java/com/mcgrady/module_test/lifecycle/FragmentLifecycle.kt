package com.mcgrady.module_test.lifecycle

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentLifecycle: FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        Log.i(f::class.simpleName, "onFragmentAttached")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Log.i(f::class.simpleName, "onFragmentCreated")
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        Log.i(f::class.simpleName, "onFragmentViewCreated")
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Log.i(f::class.simpleName, "onFragmentActivityCreated")
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        Log.i(f::class.simpleName, "onFragmentStarted")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        Log.i(f::class.simpleName, "onFragmentResumed")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        Log.i(f::class.simpleName, "onFragmentPaused")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        Log.i(f::class.simpleName, "onFragmentStopped")
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        Log.i(f::class.simpleName, "onFragmentSaveInstanceState")
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        Log.i(f::class.simpleName, "onFragmentViewDestroyed")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        Log.i(f::class.simpleName, "onFragmentDestroyed")
    }
}
