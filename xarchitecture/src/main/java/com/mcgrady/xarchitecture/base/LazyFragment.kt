package com.mcgrady.xproject.common.core.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Created by mcgrady on 2021/5/13.
 */
abstract class BaseFragment constructor(@LayoutRes private val contentLayoutId: Int = 0) : Fragment(contentLayoutId) {

    private var isLoaded = false

    override fun onResume() {
        super.onResume()
        //增加Fragment可见判断，解决Fragment嵌套Fragment下，不可见Fragment执行onResume问题
        if (!isLoaded && !isHidden) {
            lazyInit()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    private fun lazyInit() {
//        initView()
        initData()
    }

//    abstract fun initView()
    abstract fun initData()
}