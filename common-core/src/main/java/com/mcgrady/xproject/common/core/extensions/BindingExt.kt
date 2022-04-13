package com.mcgrady.xproject.common.core.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlin.reflect.KProperty


/**
 * @author skydoves (Jaewoong Eum)
 *
 * A binding extension for inflating a [layoutRes] and returns a DataBinding type [T].
 *
 * @param layoutRes The layout resource ID of the layout to inflate.
 * @param attachToParent Whether the inflated hierarchy should be attached to the parent parameter.
 *
 * @return T A DataBinding class that inflated using the [layoutRes].
 */
fun <T : ViewDataBinding> ViewGroup.binding(
    @LayoutRes layoutRes: Int,
    attachToParent: Boolean = false
): T {
    return DataBindingUtil.inflate(
        LayoutInflater.from(context), layoutRes, this, attachToParent
    )
}

///**
// * @author skydoves (Jaewoong Eum)
// *
// * Returns a binding ID by a [KProperty].
// *
// * @return A binding resource ID.
// */
//internal fun KProperty<*>.bindingId(): Int {
//    return BindingManager.getBindingIdByProperty(this)
//}