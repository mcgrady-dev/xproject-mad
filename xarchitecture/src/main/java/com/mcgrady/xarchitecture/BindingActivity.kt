package com.mcgrady.xarchitecture

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Created by mcgrady on 2021/5/9.
 */
abstract class BindingActivity<V : ViewDataBinding> constructor(@LayoutRes private val contentLayoutId: Int) :
    AppCompatActivity() {

    init {
        addOnContextAvailableListener {
            binding.notifyChange()
        }
    }

    protected var bindingComponent: DataBindingComponent? = DataBindingUtil.getDefaultComponent()

    protected val binding: V by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView(this, contentLayoutId, bindingComponent)
    }
}