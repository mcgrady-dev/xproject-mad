/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject.retromusic.fragments.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.activities.base.AbsMusicServiceActivity
import com.mcgrady.xproject.retromusic.interfaces.IMusicServiceEventListener
import com.mcgrady.xproject.retromusic.util.maybeShowAnnoyingToasts

/**
 * Created by hemanths on 18/08/17.
 */

open class AbsMusicServiceFragment(@LayoutRes layout: Int) :
    Fragment(layout),
    IMusicServiceEventListener {

    val navOptions by lazy {
        navOptions {
            launchSingleTop = false
            anim {
                enter = R.anim.retro_fragment_open_enter
                exit = R.anim.retro_fragment_open_exit
                popEnter = R.anim.retro_fragment_close_enter
                popExit = R.anim.retro_fragment_close_exit
            }
        }
    }

    var serviceActivity: AbsMusicServiceActivity? = null
        private set

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            serviceActivity = context as AbsMusicServiceActivity?
        } catch (e: ClassCastException) {
            throw RuntimeException(context.javaClass.simpleName + " must be an instance of " + AbsMusicServiceActivity::class.java.simpleName)
        }
    }

    override fun onDetach() {
        super.onDetach()
        serviceActivity = null
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serviceActivity?.addMusicServiceEventListener(this)
        maybeShowAnnoyingToasts()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        serviceActivity?.removeMusicServiceEventListener(this)
    }

    override fun onFavoriteStateChanged() {
    }

    override fun onPlayingMetaChanged() {
    }

    override fun onServiceConnected() {
    }

    override fun onServiceDisconnected() {
    }

    override fun onQueueChanged() {
    }

    override fun onPlayStateChanged() {
    }

    override fun onRepeatModeChanged() {
    }

    override fun onShuffleModeChanged() {
    }

    override fun onMediaStoreChanged() {
    }
}
