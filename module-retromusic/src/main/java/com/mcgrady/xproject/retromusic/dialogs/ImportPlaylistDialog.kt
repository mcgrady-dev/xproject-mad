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
import androidx.fragment.app.DialogFragment
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.extensions.colorButtons
import com.mcgrady.xproject.retromusic.extensions.materialDialog
import com.mcgrady.xproject.retromusic.fragments.LibraryViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ImportPlaylistDialog : DialogFragment() {
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return materialDialog(R.string.import_playlist)
            .setMessage(R.string.import_playlist_message)
            .setPositiveButton(R.string.import_label) { _, _ ->
                try {
                    libraryViewModel.importPlaylists()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .create()
            .colorButtons()
    }
}
