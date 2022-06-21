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
package com.mcgrady.xproject.retromusic.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mcgrady.xproject.retromusic.EXTRA_SONG
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.databinding.DialogPlaylistBinding
import com.mcgrady.xproject.retromusic.extensions.colorButtons
import com.mcgrady.xproject.retromusic.extensions.extra
import com.mcgrady.xproject.retromusic.extensions.materialDialog
import com.mcgrady.xproject.retromusic.fragments.LibraryViewModel
import com.mcgrady.xproject.retromusic.model.Song
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CreatePlaylistDialog : DialogFragment() {
    private var _binding: DialogPlaylistBinding? = null
    private val binding get() = _binding!!
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    companion object {
        fun create(song: Song): CreatePlaylistDialog {
            val list = mutableListOf<Song>()
            list.add(song)
            return create(list)
        }

        fun create(songs: List<Song>): CreatePlaylistDialog {
            return CreatePlaylistDialog().apply {
                arguments = bundleOf(EXTRA_SONG to songs)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogPlaylistBinding.inflate(layoutInflater)

        val songs: List<Song> = extra<List<Song>>(EXTRA_SONG).value ?: emptyList()
        val playlistView: TextInputEditText = binding.actionNewPlaylist
        val playlistContainer: TextInputLayout = binding.actionNewPlaylistContainer
        return materialDialog(R.string.new_playlist_title)
            .setView(binding.root)
            .setPositiveButton(
                R.string.create_action
            ) { _, _ ->
                val playlistName = playlistView.text.toString()
                if (!TextUtils.isEmpty(playlistName)) {
                    libraryViewModel.addToPlaylist(requireContext(), playlistName, songs)
                } else {
                    playlistContainer.error = "Playlist name can't be empty"
                }
            }
            .setNegativeButton(R.string.action_cancel, null)
            .create()
            .colorButtons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
