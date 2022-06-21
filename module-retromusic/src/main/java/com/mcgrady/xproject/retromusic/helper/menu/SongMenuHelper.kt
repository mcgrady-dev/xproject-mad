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
package com.mcgrady.xproject.retromusic.helper.menu

import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.mcgrady.xproject.retromusic.EXTRA_ALBUM_ID
import com.mcgrady.xproject.retromusic.EXTRA_ARTIST_ID
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.activities.tageditor.AbsTagEditorActivity
import com.mcgrady.xproject.retromusic.activities.tageditor.SongTagEditorActivity
import com.mcgrady.xproject.retromusic.dialogs.AddToPlaylistDialog
import com.mcgrady.xproject.retromusic.dialogs.DeleteSongsDialog
import com.mcgrady.xproject.retromusic.dialogs.SongDetailDialog
import com.mcgrady.xproject.retromusic.fragments.LibraryViewModel
import com.mcgrady.xproject.retromusic.fragments.ReloadType
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import com.mcgrady.xproject.retromusic.interfaces.IPaletteColorHolder
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.providers.BlacklistStore
import com.mcgrady.xproject.retromusic.repository.RealRepository
import com.mcgrady.xproject.retromusic.util.MusicUtil
import com.mcgrady.xproject.retromusic.util.RingtoneManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.io.File

object SongMenuHelper : KoinComponent {
    const val MENU_RES = R.menu.menu_item_song

    fun handleMenuClick(activity: FragmentActivity, song: Song, menuItemId: Int): Boolean {
        val libraryViewModel = activity.getViewModel() as LibraryViewModel
        when (menuItemId) {
            R.id.action_set_as_ringtone -> {
                if (RingtoneManager.requiresDialog(activity)) {
                    RingtoneManager.showDialog(activity)
                } else {
                    RingtoneManager.setRingtone(activity, song)
                }
                return true
            }
            R.id.action_share -> {
                activity.startActivity(
                    Intent.createChooser(
                        MusicUtil.createShareSongFileIntent(song, activity),
                        null
                    )
                )
                return true
            }
            R.id.action_delete_from_device -> {
                DeleteSongsDialog.create(song).show(activity.supportFragmentManager, "DELETE_SONGS")
                return true
            }
            R.id.action_add_to_playlist -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val playlists = get<RealRepository>().fetchPlaylists()
                    withContext(Dispatchers.Main) {
                        AddToPlaylistDialog.create(playlists, song)
                            .show(activity.supportFragmentManager, "ADD_PLAYLIST")
                    }
                }
                return true
            }
            R.id.action_play_next -> {
                MusicPlayerRemote.playNext(song)
                return true
            }
            R.id.action_add_to_current_playing -> {
                MusicPlayerRemote.enqueue(song)
                return true
            }
            R.id.action_tag_editor -> {
                val tagEditorIntent = Intent(activity, SongTagEditorActivity::class.java)
                tagEditorIntent.putExtra(AbsTagEditorActivity.EXTRA_ID, song.id)
                if (activity is IPaletteColorHolder)
                    tagEditorIntent.putExtra(
                        AbsTagEditorActivity.EXTRA_PALETTE,
                        (activity as IPaletteColorHolder).paletteColor
                    )
                activity.startActivity(tagEditorIntent)
                return true
            }
            R.id.action_details -> {
                SongDetailDialog.create(song).show(activity.supportFragmentManager, "SONG_DETAILS")
                return true
            }
            R.id.action_go_to_album -> {
                activity.findNavController(R.id.fragment_container).navigate(
                    R.id.albumDetailsFragment,
                    bundleOf(EXTRA_ALBUM_ID to song.albumId)
                )
                return true
            }
            R.id.action_go_to_artist -> {
                activity.findNavController(R.id.fragment_container).navigate(
                    R.id.artistDetailsFragment,
                    bundleOf(EXTRA_ARTIST_ID to song.artistId)
                )
                return true
            }
            R.id.action_add_to_blacklist -> {
                BlacklistStore.getInstance(activity).addPath(File(song.data))
                libraryViewModel.forceReload(ReloadType.Songs)
                return true
            }
        }
        return false
    }

    abstract class OnClickSongMenu(private val activity: FragmentActivity) :
        View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        open val menuRes: Int
            get() = MENU_RES

        abstract val song: Song

        override fun onClick(v: View) {
            val popupMenu = PopupMenu(activity, v)
            popupMenu.inflate(menuRes)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.show()
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            return handleMenuClick(activity, song, item.itemId)
        }
    }
}
