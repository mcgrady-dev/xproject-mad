package com.mcgrady.xproject.feature.musicplayer.ui.player.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mcgrady.xproject.feature.musicplayer.ui.player.MusicPlayerCoverFragment
import com.mcgrady.xproject.feature.musicplayer.ui.player.MusicPlayerLyricsFragment

class PlayerControllerPager(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MusicPlayerCoverFragment()
            1 -> MusicPlayerLyricsFragment()
            else -> MusicPlayerCoverFragment()
        }
    }

}
