package com.mcgrady.xproject.feature.musicplayer.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.media3.common.C
import androidx.media3.common.C.TRACK_TYPE_TEXT
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player.Listener
import androidx.media3.common.Tracks
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.mcgrady.xarch.extension.viewBinding
import com.mcgrady.xproject.core.base.BaseActivity
import com.mcgrady.xproject.feature.musicplayer.databinding.ActivityMusicPlayerBinding
import com.mcgrady.xproject.feature.musicplayer.service.MediaLibraryPlaybackService
import com.therouter.router.Route
import timber.log.Timber

@Route(path = "feature/musicplayer/player")
class MusicPlayerActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMusicPlayerBinding::inflate)

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null

    private val mediaItemList: MutableList<MediaItem> = mutableListOf()
    private val treePathStack: ArrayDeque<MediaItem> = ArrayDeque()
    private var subItemMediaList: MutableList<MediaItem> = mutableListOf()
    private val folderSubItemMediaList: MutableList<MediaItem> = mutableListOf()

    private lateinit var browserFuture: ListenableFuture<MediaBrowser>
    private val browser: MediaBrowser?
        get() = if (browserFuture.isDone && !browserFuture.isCancelled) browserFuture.get() else null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnStart.setOnClickListener {
            val folderSubItemMediaList = this.folderSubItemMediaList.ifEmpty { return@setOnClickListener }
            val browser = this.browser ?: return@setOnClickListener
            browser.setMediaItems(
                folderSubItemMediaList,
                /* startIndex= */ 0,
                /* startPositionMs= */ C.TIME_UNSET
            )
            browser.shuffleModeEnabled = true
            browser.prepare()
            browser.play()
            browser.sessionActivity?.send()
        }
    }


    override fun onStart() {
        super.onStart()
        initializeBrowser()
        initializeController()
    }

    private fun pushRoot(): Runnable = object: Runnable {
        override fun run() {
            if (!treePathStack.isEmpty()) {
                return
            }

            val browser = this@MusicPlayerActivity.browser ?: return
            val rootFuture = browser.getLibraryRoot(null)
            rootFuture.addListener(
                {
                    val result: LibraryResult<MediaItem> = rootFuture.get()!!
                    val root: MediaItem = result.value!!
                    pushPathStack(root)
                },
                ContextCompat.getMainExecutor(this@MusicPlayerActivity)
            )
        }

    }

    private fun displayFolder() {
        val browser = this.browser ?: return
        val id: String = if (subItemMediaList.isNotEmpty()) subItemMediaList[0].mediaId else return
        val mediaItemFuture = browser.getItem(id)
        val childrenFuture = browser.getChildren(id, 0, Int.MAX_VALUE, null)
        mediaItemFuture.addListener(
            {
                val result = mediaItemFuture.get()!!
                Timber.tag(TAG).d("media item future = $result")
            },
            ContextCompat.getMainExecutor(this)
        )
        childrenFuture.addListener(
            {
                val result = childrenFuture.get()!!
                val children = result.value!!

                folderSubItemMediaList.clear()
                folderSubItemMediaList.addAll(children)
            },
            ContextCompat.getMainExecutor(this)
        )
    }

    private fun pushPathStack(mediaItem: MediaItem) {
        treePathStack.addLast(mediaItem)
        displayChildrenList(treePathStack.last())
    }

    private fun popPathStack() {
        treePathStack.removeLast()
        if (treePathStack.size == 0) {
            finish()
            return
        }

        displayChildrenList(treePathStack.last())
    }

    private fun displayChildrenList(mediaItem: MediaItem) {
        val browser = this.browser ?: return
        val childrenFuture =
            browser.getChildren(
                mediaItem.mediaId,
                /* page= */ 0,
                /* pageSize= */ Int.MAX_VALUE,
                /* params= */ null
            )

        subItemMediaList.clear()
        childrenFuture.addListener(
            {
                val result = childrenFuture.get()!!
                val children = result.value!!
                subItemMediaList.addAll(children)
                //mediaListAdapter.notifyDataSetChanged()
                displayFolder()
            },
            ContextCompat.getMainExecutor(this)
        )
    }

    private fun initializeBrowser() {
        browserFuture = MediaBrowser.Builder(
            this,
            SessionToken(this, ComponentName(this, MediaLibraryPlaybackService::class.java))
        ).buildAsync()
        browserFuture.addListener(pushRoot(), ContextCompat.getMainExecutor(this)
        )
    }

    override fun onStop() {
        releaseBrowser()
        super.onStop()
        binding.playerView.player = null
        releaseController()
    }

    private fun initializeController() {
        controllerFuture = MediaController.Builder(
            this,
            SessionToken(this, ComponentName(this, MediaLibraryPlaybackService::class.java))
        ).buildAsync()
        controllerFuture.addListener({ setController() }, MoreExecutors.directExecutor())

    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun setController() {
        val controller = this.controller ?: return

        binding.playerView.player = controller

        updateCurrentPlayListUI()
        updateMediaMetadataUI(controller.mediaMetadata)
        binding.playerView.setShowSubtitleButton(controller.currentTracks.isTypeSupported(TRACK_TYPE_TEXT))

        controller.addListener(object: Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updateMediaMetadataUI(mediaItem?.mediaMetadata ?: MediaMetadata.EMPTY)
            }

            override fun onTracksChanged(tracks: Tracks) {
                binding.playerView.setShowSubtitleButton(tracks.isTypeSupported(TRACK_TYPE_TEXT))
            }
        })
    }

    private fun updateMediaMetadataUI(mediaMetadata: MediaMetadata) {

    }

    private fun updateCurrentPlayListUI() {
        val controller = this.controller ?: return
        mediaItemList.clear()
        for (i in 0 until controller.mediaItemCount) {
            mediaItemList.add(controller.getMediaItemAt(i))
        }
    }

    private fun releaseController() {
        MediaController.releaseFuture(controllerFuture)
    }

    private fun releaseBrowser() {
        MediaBrowser.releaseFuture(browserFuture)
    }

    companion object {
        private const val TAG = "MusicPlayer"
    }
}