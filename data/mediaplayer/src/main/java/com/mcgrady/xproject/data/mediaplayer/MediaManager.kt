package com.mcgrady.xproject.data.mediaplayer

import androidx.media3.session.MediaBrowser
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.mcgrady.xproject.data.mediaplayer.utils.toMediaItem
import com.mcgrady.xproject.data.mediaplayer.utils.toMediaItems
import java.util.concurrent.ExecutionException




object MediaManager {

    fun startQueue(browserFuture: ListenableFuture<MediaBrowser>, mediaList: List<com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child>, startIndex: Int = 0) {
        browserFuture.addListener({
            if (browserFuture.isDone) {
                val mediaBrowser: MediaBrowser? = try {
                    browserFuture.get()
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                    null
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    null
                }
                mediaBrowser?.run {
                    clearMediaItems()
                    setMediaItems(mediaList.toMediaItems())
                    prepare()
                    seekTo(startIndex, 0)
                    play()
                    enqueueDatabase(mediaList, true, 0)
                }
            }
        }, MoreExecutors.directExecutor())
    }

    fun startQueue(browserFuture: ListenableFuture<MediaBrowser>, media: com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child) {
        browserFuture.addListener({
            if (browserFuture.isDone) {
                val mediaBrowser: MediaBrowser? = try {
                    browserFuture.get()
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                    null
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    null
                }
                mediaBrowser?.run {
                    clearMediaItems()
                    setMediaItem(media.toMediaItem())
                    prepare()
                    play()
                    enqueueDatabase(media, true, 0)
                }
            }
        }, MoreExecutors.directExecutor())
    }

    private fun enqueueDatabase(media: com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child, reset: Boolean, afterIndex: Int) {

    }
    private fun enqueueDatabase(mediaList: List<com.mcgrady.xproject.data.mediaplayer.model.subsonic.Child>, reset: Boolean, afterIndex: Int) {

    }
}