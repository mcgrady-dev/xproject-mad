package com.mcgrady.xproject.testing.samples.fragment.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mcgrady.xproject.testing.samples.R

class OneFragment : Fragment() {

    companion object {
        fun newInstance(param: String) = OneFragment().apply {
            val args = Bundle()
            args.putString("param_text", param)
            arguments = args
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        val tvMessage = view.findViewById<TextView>(R.id.message)
        tvMessage.text = arguments?.getString("param_text")

        return view
    }
}