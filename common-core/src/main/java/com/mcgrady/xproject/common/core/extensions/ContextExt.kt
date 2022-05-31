package com.mcgrady.xproject.common.core.extensions

import android.app.Application
import androidx.fragment.app.FragmentActivity
import com.zackratos.ultimatebarx.ultimatebarx.bean.BarConfig
import com.zackratos.ultimatebarx.ultimatebarx.navigationBar
import com.zackratos.ultimatebarx.ultimatebarx.statusBar


fun Application.ActivityLifecycleCallbacks.statusBar(activity: FragmentActivity, block: (BarConfig.() -> Unit)? = null) = activity.statusBar(block)

fun Application.ActivityLifecycleCallbacks.navigationBar(activity: FragmentActivity, block: (BarConfig.() -> Unit)? = null) = activity.navigationBar(block)
