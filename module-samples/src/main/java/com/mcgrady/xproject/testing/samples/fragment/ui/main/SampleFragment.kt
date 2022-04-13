package com.mcgrady.xproject.testing.samples.fragment.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mcgrady.xproject.testing.samples.fragment.R

class SampleFragment : Fragment() {

    companion object {
        fun newInstance() = SampleFragment()
    }

    private lateinit var viewModel: SampleFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SampleFragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}