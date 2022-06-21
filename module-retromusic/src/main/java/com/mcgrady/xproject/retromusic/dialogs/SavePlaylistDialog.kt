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
import android.media.MediaScannerConnection
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.mcgrady.xproject.retromusic.EXTRA_PLAYLIST
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.db.PlaylistWithSongs
import com.mcgrady.xproject.retromusic.extensions.*
import com.mcgrady.xproject.retromusic.helper.M3UWriter
import com.mcgrady.xproject.retromusic.util.PlaylistsUtil
import com.mcgrady.xproject.theme.util.VersionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavePlaylistDialog : DialogFragment() {
    companion object {
        fun create(playlistWithSongs: PlaylistWithSongs): SavePlaylistDialog {
            return SavePlaylistDialog().apply {
                arguments = bundleOf(
                    EXTRA_PLAYLIST to playlistWithSongs
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playlistWithSongs = extraNotNull<PlaylistWithSongs>(EXTRA_PLAYLIST).value

        if (VersionUtils.hasR()) {
            createNewFile(
                "audio/mpegurl",
                playlistWithSongs.playlistEntity.playlistName
            ) { outputStream, data ->
                try {
                    if (outputStream != null) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            M3UWriter.writeIO(
                                outputStream,
                                playlistWithSongs
                            )
                            withContext(Dispatchers.Main) {
                                showToast(
                                    requireContext().getString(
                                        R.string.saved_playlist_to,
                                        data?.lastPathSegment
                                    ),
                                    Toast.LENGTH_LONG
                                )
                                dismiss()
                            }
                        }
                    }
                } catch (e: Exception) {
                    showToast(
                        "Something went wrong : " + e.message
                    )
                }
            }
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                val file = PlaylistsUtil.savePlaylistWithSongs(playlistWithSongs)
                MediaScannerConnection.scanFile(
                    requireActivity(),
                    arrayOf<String>(file.path),
                    null
                ) { _, _ ->
                }
                withContext(Dispatchers.Main) {
                    showToast(
                        getString(R.string.saved_playlist_to, file),
                        Toast.LENGTH_LONG
                    )
                    dismiss()
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return materialDialog(R.string.save_playlist_title)
            .setView(R.layout.loading)
            .create().colorButtons()
    }
}
