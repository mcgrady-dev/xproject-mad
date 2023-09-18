package com.mcgrady.xproject.feature.musicplayer.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DataSourceBitmapLoader
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CacheBitmapLoader
import androidx.media3.session.CommandButton
import androidx.media3.session.LibraryResult
import androidx.media3.session.LibraryResult.RESULT_ERROR_NOT_SUPPORTED
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.mcgrady.xproject.feature.musicplayer.R
import com.mcgrady.xproject.feature.musicplayer.ui.MusicPlayerActivity

class MediaLibraryPlaybackService : MediaLibraryService() {

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaLibrarySession
    private val sessionCallback = MediaSessionCallbackImpl()
    private lateinit var customCommands: List<CommandButton>
    private var customLayout = ImmutableList.of<CommandButton>()

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate() {
        super.onCreate()
        customCommands =
            listOf(
                getShuffleCommandButton(
                    SessionCommand(CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_ON, Bundle.EMPTY)
                ),
                getShuffleCommandButton(
                    SessionCommand(CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_OFF, Bundle.EMPTY)
                )
            )
        customLayout = ImmutableList.of(customCommands[0])
        initSessionAndPlayer()
        setListener(MediaSessionServiceListener())
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initSessionAndPlayer() {
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()

        com.mcgrady.xproject.data.mediaplayer.MediaItemTree.initialize(assets)

        mediaSession = MediaLibrarySession.Builder(this, player, sessionCallback)
            .setSessionActivity(findSingleTopActivity())
            .setBitmapLoader(CacheBitmapLoader(DataSourceBitmapLoader(this)))
            .build()
        if (!customLayout.isEmpty()) {
            // Send custom layout to legacy session.
            mediaSession.setCustomLayout(customLayout)
        }
    }

    private fun findSingleTopActivity(): PendingIntent {
        return PendingIntent.getActivity(
            this,
            0,
            Intent(this, MusicPlayerActivity::class.java),
            immutableFlag or FLAG_UPDATE_CURRENT
        )
    }

    private fun getShuffleCommandButton(sessionCommand: SessionCommand): CommandButton {
        val isOn = sessionCommand.customAction == CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_ON
        return CommandButton.Builder()
            .setDisplayName(
                getString(
                    if (isOn) androidx.media3.ui.R.string.exo_controls_shuffle_on_description
                    else androidx.media3.ui.R.string.exo_controls_shuffle_off_description
                )
            )
            .setSessionCommand(sessionCommand)
            .setIconResId(if (isOn) androidx.media3.ui.R.drawable.exo_icon_shuffle_off else androidx.media3.ui.R.drawable.exo_icon_shuffle_on)
            .build()
    }

    private fun ignoreFuture(customLayout: ListenableFuture<SessionResult>) {
        /* Do nothing. */
    }

    private fun findBackStackedActivity(): PendingIntent {
        return android.app.TaskStackBuilder.create(this).run {
            //addNextIntent(Intent(this@PlaybackService, MainActivity::class.java))
            addNextIntent(Intent(this@MediaLibraryPlaybackService, MusicPlayerActivity::class.java))
            getPendingIntent(0, immutableFlag or FLAG_UPDATE_CURRENT)
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (!player.playWhenReady || player.mediaItemCount == 0) {
            stopSelf()
        }
    }


    @SuppressLint("UnsafeOptInUsageError")
    override fun onDestroy() {
        mediaSession.setSessionActivity(findBackStackedActivity())
        mediaSession.release()
        clearListener()
        super.onDestroy()
    }

    private inner class MediaSessionCallbackImpl : MediaLibrarySession.Callback {

        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): MediaSession.ConnectionResult {
            val connectionResult = super.onConnect(session, controller)
            val availableSessionCommands = connectionResult.availableSessionCommands.buildUpon()
            for (commandButton in customCommands) {
                // Add custom command to available session commands.
                commandButton.sessionCommand?.let { availableSessionCommands.add(it) }
            }
            return MediaSession.ConnectionResult.accept(
                availableSessionCommands.build(),
                connectionResult.availablePlayerCommands
            )
        }

        override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
            if (!customLayout.isEmpty() && controller.controllerVersion != 0) {
                // Let Media3 controller (for instance the MediaNotificationProvider) know about the custom
                // layout right after it connected.
                ignoreFuture(mediaSession.setCustomLayout(controller, customLayout))
            }
        }

        override fun onCustomCommand(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            customCommand: SessionCommand,
            args: Bundle
        ): ListenableFuture<SessionResult> {
            if (CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_ON == customCommand.customAction) {
                // Enable shuffling.
                player.shuffleModeEnabled = true
                // Change the custom layout to contain the `Disable shuffling` command.
                customLayout = ImmutableList.of(customCommands[1])
                // Send the updated custom layout to controllers.
                session.setCustomLayout(customLayout)
            } else if (CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_OFF == customCommand.customAction) {
                // Disable shuffling.
                player.shuffleModeEnabled = false
                // Change the custom layout to contain the `Enable shuffling` command.
                customLayout = ImmutableList.of(customCommands[0])
                // Send the updated custom layout to controllers.
                session.setCustomLayout(customLayout)
            }
            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
        }

        override fun onGetLibraryRoot(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<MediaItem>> {
            if (params != null && params.isRecent) {
                // The service currently does not support playback resumption. Tell System UI by returning
                // an error of type 'RESULT_ERROR_NOT_SUPPORTED' for a `params.isRecent` request. See
                // https://github.com/androidx/media/issues/355
                return Futures.immediateFuture(LibraryResult.ofError(RESULT_ERROR_NOT_SUPPORTED))
            }
            return Futures.immediateFuture(LibraryResult.ofItem(com.mcgrady.xproject.data.mediaplayer.MediaItemTree.getRootItem(), params))
        }

        override fun onGetItem(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            mediaId: String
        ): ListenableFuture<LibraryResult<MediaItem>> {
            val item =
                com.mcgrady.xproject.data.mediaplayer.MediaItemTree.getItem(mediaId)
                    ?: return Futures.immediateFuture(
                        LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)
                    )
            return Futures.immediateFuture(LibraryResult.ofItem(item, /* params= */ null))
        }

        override fun onSubscribe(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            parentId: String,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<Void>> {
            val children =
                com.mcgrady.xproject.data.mediaplayer.MediaItemTree.getChildren(parentId)
                    ?: return Futures.immediateFuture(
                        LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)
                    )
            session.notifyChildrenChanged(browser, parentId, children.size, params)
            return Futures.immediateFuture(LibraryResult.ofVoid())
        }

        override fun onGetChildren(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            parentId: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
            val children =
                com.mcgrady.xproject.data.mediaplayer.MediaItemTree.getChildren(parentId)
                    ?: return Futures.immediateFuture(
                        LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)
                    )

            return Futures.immediateFuture(LibraryResult.ofItemList(children, params))
        }

        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: List<MediaItem>
        ): ListenableFuture<List<MediaItem>> {
            val updatedMediaItems: List<MediaItem> =
                mediaItems.map { mediaItem ->
                    if (mediaItem.requestMetadata.searchQuery != null)
                        getMediaItemFromSearchQuery(mediaItem.requestMetadata.searchQuery!!)
                    else com.mcgrady.xproject.data.mediaplayer.MediaItemTree.getItem(mediaItem.mediaId) ?: mediaItem
                }
            return Futures.immediateFuture(updatedMediaItems)
        }

        private fun getMediaItemFromSearchQuery(query: String): MediaItem {
            // Only accept query with pattern "play [Title]" or "[Title]"
            // Where [Title]: must be exactly matched
            // If no media with exact name found, play a random media instead
            val mediaTitle =
                if (query.startsWith("play ", ignoreCase = true)) {
                    query.drop(5)
                } else {
                    query
                }

            return com.mcgrady.xproject.data.mediaplayer.MediaItemTree.getItemFromTitle(mediaTitle) ?: com.mcgrady.xproject.data.mediaplayer.MediaItemTree.getRandomItem()
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private inner class MediaSessionServiceListener : Listener {

        /**
         * This method is only required to be implemented on Android 12 or above when an attempt is made
         * by a media controller to resume playback when the {@link MediaSessionService} is in the
         * background.
         */
        @SuppressLint("MissingPermission") // TODO: b/280766358 - Request this permission at runtime.
        override fun onForegroundServiceStartNotAllowedException() {
            val notificationManager = NotificationManagerCompat.from(this@MediaLibraryPlaybackService)
            ensureNotificationChannel(notificationManager)
            val pendingIntent = TaskStackBuilder.create(this@MediaLibraryPlaybackService).run {
                addNextIntent(Intent(this@MediaLibraryPlaybackService, MusicPlayerActivity::class.java))
                getPendingIntent(0, immutableFlag or FLAG_UPDATE_CURRENT)
            }
            val builder = NotificationCompat.Builder(this@MediaLibraryPlaybackService, com.mcgrady.xproject.data.mediaplayer.notification.PlayingNotification.NOTIFICATION_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(androidx.media3.session.R.drawable.media3_notification_small_icon)
                .setContentTitle(getString(R.string.notification_content_title))
                .setStyle(
                    NotificationCompat.BigTextStyle().bigText(getString(R.string.notification_content_text))
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
            notificationManager.notify(com.mcgrady.xproject.data.mediaplayer.notification.PlayingNotification.NOTIFICATION_ID, builder.build())
        }
    }

    private fun ensureNotificationChannel(notificationManager: NotificationManagerCompat) {
        if (Build.VERSION.SDK_INT < 26 || notificationManager.getNotificationChannel(com.mcgrady.xproject.data.mediaplayer.notification.PlayingNotification.NOTIFICATION_CHANNEL_ID) != null) {
            return
        }

        val channel = NotificationChannel(
            com.mcgrady.xproject.data.mediaplayer.notification.PlayingNotification.NOTIFICATION_CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private val immutableFlag = if (Build.VERSION.SDK_INT >= 23) FLAG_IMMUTABLE else 0
        private const val CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_ON =
            "android.media3.session.demo.SHUFFLE_ON"
        private const val CUSTOM_COMMAND_TOGGLE_SHUFFLE_MODE_OFF =
            "android.media3.session.demo.SHUFFLE_OFF"
    }
}