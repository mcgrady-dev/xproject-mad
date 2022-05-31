package com.mcgrady.xarchitecture.databind

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.mcgrady.xarchitecture.delegate.FragmentDelegate
import com.mcgrady.xarchitecture.ext.inflateMethod
import kotlin.reflect.KProperty

/**
 * Created by mcgrady on 2021/7/19.
 */
class FragmentDataBinding<T: ViewDataBinding>(
    classes: Class<T>,
    val fragment: Fragment,
    private var block: (T.() -> Unit)? = null
) : FragmentDelegate<T>(fragment) {

    private val layoutInflater = classes.inflateMethod()

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return binding?.run {
            return this
        } ?: let {

            try {
                /**
                 * 检查目的，是为了防止在 onCreateView() or after onDestroyView() 使用 binding。
                 * 另外在销毁之后，如果再次使用，由于 delegate property 会被再次初始化出现的异常
                 *
                 * 捕获这个异常的原因，是为了兼容之前的版本，防止因为升级，造成崩溃
                 */
                check(thisRef.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                    "cannot use binding in before onCreateView() or after onDestroyView() from 1.1.4. about [issue](https://github.com/hi-dhl/Binding/issues/31#issuecomment-1109733307)"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val bind: T = if (thisRef.view == null) {
                @Suppress("UNCHECKED_CAST")
                layoutInflater.invoke(null, thisRef.layoutInflater) as T
            } else {
                DataBindingUtil.bind(thisRef.requireView()) ?: throw RuntimeException("cannot use binding in FragmentDataBinding")
            }

            return bind.apply {
                binding = this
                lifecycleOwner = fragment.viewLifecycleOwner
                block?.invoke(this)
                block = null
            }
        }
    }
}