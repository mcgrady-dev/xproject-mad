package com.mcgrady.xproject.testing.samples.fragment.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mcgrady.xproject.testing.samples.R


class TwoFragment constructor() : Fragment() {

    private var param: String? = null

    constructor(param: String) : this() {
        this.param = param
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        val tvMessage = view.findViewById<TextView>(R.id.message)
        tvMessage.text = param

        return view
    }
}