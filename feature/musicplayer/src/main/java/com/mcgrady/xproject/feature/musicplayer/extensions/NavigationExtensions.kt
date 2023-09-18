package com.mcgrady.xproject.feature.musicplayer.extensions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.animation.doOnEnd
import androidx.core.view.drawToBitmap
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigationrail.NavigationRailView
import com.mcgrady.xproject.feature.musicplayer.R

/**
 * Potentially animate showing a [BottomNavigationView].
 *
 * Abruptly changing the visibility leads to a re-layout of main content, animating
 * `translationY` leaves a gap where the view was that content does not fill.
 *
 * Instead, take a snapshot of the view, and animate this in, only changing the visibility (and
 * thus layout) when the animation completes.
 */
fun NavigationBarView.show() {
    if (this is NavigationRailView) return
    if (isVisible) return

    val parent = parent as ViewGroup
    // View needs to be laid out to create a snapshot & know position to animate. If view isn't
    // laid out yet, need to do this manually.
    if (!isLaidOut) {
        measure(
            View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.AT_MOST)
        )
        layout(parent.left, parent.height - measuredHeight, parent.right, parent.height)
    }

    val drawable = BitmapDrawable(context.resources, drawToBitmap())
    drawable.setBounds(left, parent.height, right, parent.height + height)
    parent.overlay.add(drawable)
    ValueAnimator.ofInt(parent.height, top).apply {
        duration = ANIM_DURATION
        interpolator = AnimationUtils.loadInterpolator(
            context,
            R.anim.m3_motion_easing_standard
        )
        addUpdateListener {
            val newTop = it.animatedValue as Int
            drawable.setBounds(left, newTop, right, newTop + height)
        }
        doOnEnd {
            parent.overlay.remove(drawable)
            isVisible = true
        }
        start()
    }
}

/**
 * Potentially animate hiding a [BottomNavigationView].
 *
 * Abruptly changing the visibility leads to a re-layout of main content, animating
 * `translationY` leaves a gap where the view was that content does not fill.
 *
 * Instead, take a snapshot, instantly hide the view (so content lays out to fill), then animate
 * out the snapshot.
 */
fun NavigationBarView.hide() {
    if (this is NavigationRailView) return
    if (isGone) return

    if (!isLaidOut) {
        isGone = true
        return
    }

    val drawable = BitmapDrawable(context.resources, drawToBitmap())
    val parent = parent as ViewGroup
    drawable.setBounds(left, top, right, bottom)
    parent.overlay.add(drawable)
    isGone = true
    ValueAnimator.ofInt(top, parent.height).apply {
        duration = ANIM_DURATION
        interpolator = AnimationUtils.loadInterpolator(
            context,
            R.anim.m3_motion_easing_standard
        )
        addUpdateListener {
            val newTop = it.animatedValue as Int
            drawable.setBounds(left, newTop, right, newTop + height)
        }
        doOnEnd {
            parent.overlay.remove(drawable)
        }
        start()
    }
}

fun BottomSheetBehavior<*>.peekHeightAnimate(context: Context, value: Int): Animator {
    return ObjectAnimator.ofInt(this, "peekHeight", value)
        .apply {
            duration = ANIM_DURATION
            interpolator = AnimationUtils.loadInterpolator(
                context,
                R.anim.m3_motion_easing_standard
            )
            start()
        }
}

const val ANIM_DURATION = 300L