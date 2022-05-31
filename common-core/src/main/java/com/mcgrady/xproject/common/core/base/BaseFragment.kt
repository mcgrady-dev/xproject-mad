package com.mcgrady.xproject.common.core.base

import com.mcgrady.xarchitecture.base.LazyFragment

/**
 * Created by mcgrady on 2021/5/13.
 */
abstract class BaseFragment : LazyFragment() {

    override fun lazyInit() {
        initData()
    }

    abstract fun initData()
}