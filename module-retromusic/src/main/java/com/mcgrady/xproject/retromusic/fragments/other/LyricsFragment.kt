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
package com.mcgrady.xproject.retromusic.fragments.other

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.afollestad.materialdialogs.input.input
import com.google.android.material.tabs.TabLayoutMediator
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.activities.MainActivity
import com.mcgrady.xproject.retromusic.activities.tageditor.TagWriter
import com.mcgrady.xproject.retromusic.databinding.FragmentLyricsBinding
import com.mcgrady.xproject.retromusic.databinding.FragmentNormalLyricsBinding
import com.mcgrady.xproject.retromusic.databinding.FragmentSyncedLyricsBinding
import com.mcgrady.xproject.retromusic.extensions.*
import com.mcgrady.xproject.retromusic.fragments.base.AbsMainActivityFragment
import com.mcgrady.xproject.retromusic.fragments.base.AbsMusicServiceFragment
import com.mcgrady.xproject.retromusic.helper.MusicPlayerRemote
import com.mcgrady.xproject.retromusic.helper.MusicProgressViewUpdateHelper
import com.mcgrady.xproject.retromusic.lyrics.LrcView
import com.mcgrady.xproject.retromusic.model.AudioTagInfo
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.util.FileUtils
import com.mcgrady.xproject.retromusic.util.LyricUtil
import com.mcgrady.xproject.retromusic.util.UriUtil
import com.mcgrady.xproject.theme.common.ATHToolbarActivity
import com.mcgrady.xproject.theme.util.ToolbarContentTintHelper
import com.mcgrady.xproject.theme.util.VersionUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File
import java.io.FileOutputStream
import java.util.*

class LyricsFragment : AbsMainActivityFragment(R.layout.fragment_lyrics) {

    private var _binding: FragmentLyricsBinding? = null
    private val binding get() = _binding!!
    private lateinit var song: Song

    private lateinit var lyricsSectionsAdapter: LyricsSectionsAdapter

    private val googleSearchLrcUrl: String
        get() {
            var baseUrl = "http://www.google.com/search?"
            var query = song.title + "+" + song.artistName
            query = "q=" + query.replace(" ", "+") + " lyrics"
            baseUrl += query
            return baseUrl
        }

    private lateinit var normalLyricsLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var newSyncedLyricsLauncher: ActivityResultLauncher<Intent>
    private lateinit var editSyncedLyricsLauncher: ActivityResultLauncher<IntentSenderRequest>

    private lateinit var cacheFile: File
    private var syncedLyrics: String = ""
    private lateinit var syncedFileUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Normal lyrics launcher
        normalLyricsLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    FileUtils.copyFileToUri(requireContext(), cacheFile, song.uri)
                }
            }
        newSyncedLyricsLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    context?.contentResolver?.openOutputStream(result.data?.data!!)?.use {
                        it.write(syncedLyrics.toByteArray())
                    }
                }
            }
        editSyncedLyricsLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    requireContext().contentResolver.openOutputStream(syncedFileUri)?.use { os ->
                        (os as FileOutputStream).channel.truncate(0)
                        os.write(syncedLyrics.toByteArray())
                        os.flush()
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition = Fade()
        exitTransition = Fade()
        updateTitleSong()
        lyricsSectionsAdapter = LyricsSectionsAdapter(requireActivity())
        _binding = FragmentLyricsBinding.bind(view)
        binding.container.transitionName = "lyrics"

        setupWakelock()
        setupViews()
        setupToolbar()
    }

    private fun setupViews() {
        binding.lyricsPager.adapter = lyricsSectionsAdapter
        TabLayoutMediator(binding.tabLyrics, binding.lyricsPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.synced_lyrics)
                1 -> getString(R.string.normal_lyrics)
                else -> ""
            }
        }.attach()
//        lyricsPager.isUserInputEnabled = false

        binding.tabLyrics.setSelectedTabIndicatorColor(accentColor())
        binding.tabLyrics.setTabTextColors(textColorSecondary(), accentColor())
        binding.editButton.accentColor()
        binding.editButton.setOnClickListener {
            when (binding.lyricsPager.currentItem) {
                0 -> {
                    editSyncedLyrics()
                }
                1 -> {
                    editNormalLyrics()
                }
            }
        }
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateTitleSong()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateTitleSong()
    }

    private fun updateTitleSong() {
        song = MusicPlayerRemote.currentSong
    }

    private fun setupToolbar() {
        mainActivity.setSupportActionBar(binding.toolbar)
        ToolbarContentTintHelper.colorBackButton(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupWakelock() {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
            requireContext(),
            binding.toolbar,
            menu,
            ATHToolbarActivity.getToolbarBackgroundColor(binding.toolbar)
        )
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            openUrl(googleSearchLrcUrl)
        }
        return false
    }

    @SuppressLint("CheckResult")
    private fun editNormalLyrics() {
        var content = ""
        val file = File(MusicPlayerRemote.currentSong.data)
        try {
            content = AudioFileIO.read(file).tagOrCreateDefault.getFirst(FieldKey.LYRICS)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        materialDialog().show {
            title(res = R.string.edit_normal_lyrics)
            input(
                hintRes = R.string.paste_lyrics_here,
                prefill = content,
                inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT
            ) { _, input ->
                val fieldKeyValueMap = EnumMap<FieldKey, String>(FieldKey::class.java)
                fieldKeyValueMap[FieldKey.LYRICS] = input.toString()
                syncedLyrics = input.toString()
                GlobalScope.launch {
                    if (VersionUtils.hasR()) {
                        cacheFile = TagWriter.writeTagsToFilesR(
                            requireContext(),
                            AudioTagInfo(
                                listOf(song.data), fieldKeyValueMap, null
                            )
                        )[0]
                        val pendingIntent =
                            MediaStore.createWriteRequest(
                                requireContext().contentResolver,
                                listOf(song.uri)
                            )

                        normalLyricsLauncher.launch(
                            IntentSenderRequest.Builder(pendingIntent).build()
                        )
                    } else {
                        TagWriter.writeTagsToFiles(
                            requireContext(),
                            AudioTagInfo(
                                listOf(song.data), fieldKeyValueMap, null
                            )
                        )
                    }
                }
            }
            positiveButton(res = R.string.save) {
                (lyricsSectionsAdapter.fragments[1].first as NormalLyrics).loadNormalLyrics()
            }
            negativeButton(res = android.R.string.cancel)
        }
    }

    @SuppressLint("CheckResult")
    private fun editSyncedLyrics() {
        val content: String = LyricUtil.getStringFromLrc(LyricUtil.getSyncedLyricsFile(song))

        materialDialog().show {
            title(res = R.string.edit_synced_lyrics)
            input(
                hintRes = R.string.paste_timeframe_lyrics_here,
                prefill = content,
                inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT
            ) { _, input ->
                if (VersionUtils.hasR()) {
                    syncedLyrics = input.toString()
                    val lrcFile = LyricUtil.getSyncedLyricsFile(song)
                    if (lrcFile?.exists() == true) {
                        syncedFileUri =
                            UriUtil.getUriFromPath(requireContext(), lrcFile.absolutePath)
                        val pendingIntent =
                            MediaStore.createWriteRequest(
                                requireContext().contentResolver,
                                listOf(syncedFileUri)
                            )
                        editSyncedLyricsLauncher.launch(
                            IntentSenderRequest.Builder(pendingIntent).build()
                        )
                    } else {
                        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        intent.type = "*/*"
                        intent.putExtra(
                            Intent.EXTRA_TITLE,
                            LyricUtil.getLrcOriginalPath(File(song.data).name)
                        )
                        newSyncedLyricsLauncher.launch(intent)
                    }
                } else {
                    LyricUtil.writeLrc(song, input.toString())
                }
            }
            positiveButton(res = R.string.save) {
                (lyricsSectionsAdapter.fragments[0].first as SyncedLyrics).loadLRCLyrics()
            }
            negativeButton(res = android.R.string.cancel)
        }
    }

    class LyricsSectionsAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        val fragments = listOf(
            Pair(SyncedLyrics(), R.string.synced_lyrics),
            Pair(NormalLyrics(), R.string.normal_lyrics)
        )

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position].first
        }
    }

    class NormalLyrics : AbsMusicServiceFragment(R.layout.fragment_normal_lyrics) {

        private var _binding: FragmentNormalLyricsBinding? = null
        private val binding get() = _binding!!

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            _binding = FragmentNormalLyricsBinding.bind(view)
            loadNormalLyrics()
            super.onViewCreated(view, savedInstanceState)
        }

        fun loadNormalLyrics() {
            var lyrics: String? = null
            val file = File(MusicPlayerRemote.currentSong.data)
            try {
                lyrics = AudioFileIO.read(file).tagOrCreateDefault.getFirst(FieldKey.LYRICS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            binding.noLyricsFound.isVisible = lyrics.isNullOrEmpty()
            binding.normalLyrics.text = lyrics
        }

        override fun onPlayingMetaChanged() {
            super.onPlayingMetaChanged()
            loadNormalLyrics()
        }

        override fun onServiceConnected() {
            super.onServiceConnected()
            loadNormalLyrics()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }

    class SyncedLyrics :
        AbsMusicServiceFragment(R.layout.fragment_synced_lyrics),
        MusicProgressViewUpdateHelper.Callback {

        private var _binding: FragmentSyncedLyricsBinding? = null
        private val binding get() = _binding!!
        private lateinit var updateHelper: MusicProgressViewUpdateHelper

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            updateHelper = MusicProgressViewUpdateHelper(this, 500, 1000)
            _binding = FragmentSyncedLyricsBinding.bind(view)
            setupLyricsView()
            loadLRCLyrics()
            super.onViewCreated(view, savedInstanceState)
        }

        fun loadLRCLyrics() {
            binding.lyricsView.setLabel(getString(R.string.empty))
            LyricUtil.getSyncedLyricsFile(MusicPlayerRemote.currentSong)?.let {
                binding.lyricsView.loadLrc(it)
            }
        }

        private fun setupLyricsView() {
            binding.lyricsView.apply {
                setCurrentColor(accentColor())
                setTimeTextColor(accentColor())
                setTimelineColor(accentColor())
                setTimelineTextColor(accentColor())
                setDraggable(
                    true,
                    LrcView.OnPlayClickListener {
                        MusicPlayerRemote.seekTo(it.toInt())
                        return@OnPlayClickListener true
                    }
                )
            }
        }

        override fun onUpdateProgressViews(progress: Int, total: Int) {
            binding.lyricsView.updateTime(progress.toLong())
        }

        override fun onPlayingMetaChanged() {
            super.onPlayingMetaChanged()
            loadLRCLyrics()
        }

        override fun onServiceConnected() {
            super.onServiceConnected()
            loadLRCLyrics()
        }

        override fun onResume() {
            super.onResume()
            updateHelper.start()
        }

        override fun onPause() {
            super.onPause()
            updateHelper.stop()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (MusicPlayerRemote.playingQueue.isNotEmpty())
            (requireActivity() as MainActivity).expandPanel()
        _binding = null
    }
}
