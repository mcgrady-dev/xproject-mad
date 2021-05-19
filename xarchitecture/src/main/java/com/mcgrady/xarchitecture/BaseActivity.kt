package com.mcgrady.xarchitecture

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

/**
 * Created by mcgrady on 5/5/21.
 */
abstract class BaseActivity<V : ViewDataBinding> constructor(@LayoutRes private val contentLayoutId: Int) :
    BindingActivity<V>(contentLayoutId) {

    abstract fun observeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        initView(savedInstanceState)
        observeViewModel()
    }

    abstract fun initView(savedInstanceState: Bundle?)
}