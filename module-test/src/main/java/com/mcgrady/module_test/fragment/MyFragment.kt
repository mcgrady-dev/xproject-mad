package com.mcgrady.module_test.fragment

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

import com.mcgrady.module_test.R

/**
 * Created by mcgrady on 1/29/21.
 */
class MyFragment : Fragment(R.layout.fragment_my) {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }
}