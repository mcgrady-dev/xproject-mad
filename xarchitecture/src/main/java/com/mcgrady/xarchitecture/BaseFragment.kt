package com.mcgrady.xarchitecture

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

/**
 * Created by mcgrady on 2021/5/13.
 */
abstract class BaseFragment<V : ViewDataBinding> constructor(@LayoutRes private val contentLayoutId: Int) : BindingFragment<V>(contentLayoutId) {

    private var isLoaded = false

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            lazyInit()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    abstract fun lazyInit()
}