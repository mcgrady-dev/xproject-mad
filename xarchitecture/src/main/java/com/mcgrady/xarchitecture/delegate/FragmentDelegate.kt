package com.mcgrady.xarchitecture.base.delegate

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.mcgrady.xarchitecture.ext.observerWhenCreated
import kotlin.properties.ReadOnlyProperty

/**
 * Created by mcgrady on 2021/7/19.
 */
abstract class FragmentDelegate<T : ViewBinding>(
    fragment: Fragment
) : ReadOnlyProperty<Fragment, T> {

    protected var binding: T? = null

    init {

        // 详情查看 [issue][https://github.com/hi-dhl/Binding/issues/31#issuecomment-1109729949]
        fragment.lifecycle.observerWhenCreated {
            val fragmentManager = fragment.parentFragmentManager
            fragmentManager.registerFragmentLifecycleCallbacks(object :
                FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentViewDestroyed(fm, f)
                    // 检查 fragment 的目的，为了防止类似于加载多个 Fragment 场景销毁的时候，出现不必要的异常
                    if (f == fragment) {
                        destroyed()
                        fragmentManager.unregisterFragmentLifecycleCallbacks(this)
                    }

                }
            }, false)
        }

    }

    private fun destroyed() {
        binding = null
    }
}