@file:Suppress("NOTHING_TO_INLINE")

package com.mcgrady.xarchitecture.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by mcgrady on 2022/5/31.
 */

//设置ViewPager2的过度滚动模式为绝不允许用户过度滚动此视图
//@kotlin.internal.InlineOnly
inline fun ViewPager2.overScrollNever() {
    val childView: View = this.getChildAt(0)
    if (childView is RecyclerView) {
        childView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }
}

//设置ViewPager2的过度滚动模式为始终允许用户过度滚动此视图，前提是它是可以滚动的视图
//@kotlin.internal.InlineOnly
inline fun ViewPager2.overScrollAlways() {
    val childView: View = this.getChildAt(0)
    if (childView is RecyclerView) {
        childView.overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS
    }
}

//设置ViewPager2的过度滚动模式为仅当内容大到足以有意义地滚动时，才允许用户过度滚动此视图，前提是它是可以滚动的视图
//@kotlin.internal.InlineOnly
inline fun ViewPager2.overScrollIfContentScrolls() {
    val childView: View = this.getChildAt(0)
    if (childView is RecyclerView) {
        childView.overScrollMode = RecyclerView.OVER_SCROLL_IF_CONTENT_SCROLLS
    }
}