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
package com.mcgrady.xproject.retromusic.service.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.activities.MainActivity
import com.mcgrady.xproject.retromusic.glide.GlideApp
import com.mcgrady.xproject.retromusic.glide.RetroGlideExtension
import com.mcgrady.xproject.retromusic.model.Song
import com.mcgrady.xproject.retromusic.service.MusicService
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.ACTION_QUIT
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.ACTION_REWIND
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.ACTION_SKIP
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.ACTION_TOGGLE_PAUSE
import com.mcgrady.xproject.retromusic.service.MusicService.Companion.TOGGLE_FAVORITE
import com.mcgrady.xproject.retromusic.util.PreferenceUtil
import com.mcgrady.xproject.theme.util.VersionUtils

@SuppressLint("RestrictedApi")
class PlayingNotificationImpl24(
    val context: MusicService,
    mediaSessionToken: MediaSessionCompat.Token,
) : PlayingNotification(context) {

    init {
        val action = Intent(context, MainActivity::class.java)
        action.putExtra(MainActivity.EXPAND_PANEL, PreferenceUtil.isExpandPanel)
        action.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val clickIntent =
            PendingIntent.getActivity(
                context,
                0,
                action,
                PendingIntent.FLAG_UPDATE_CURRENT or if (VersionUtils.hasMarshmallow())
                    PendingIntent.FLAG_IMMUTABLE
                else 0
            )

        val serviceName = ComponentName(context, MusicService::class.java)
        val intent = Intent(ACTION_QUIT)
        intent.component = serviceName
        val deleteIntent = PendingIntent.getService(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (
                if (VersionUtils.hasMarshmallow())
                    PendingIntent.FLAG_IMMUTABLE
                else 0
                )
        )
        val toggleFavorite = buildFavoriteAction(false)
        val playPauseAction = buildPlayAction(true)
        val previousAction = NotificationCompat.Action(
            R.drawable.ic_skip_previous_round_white_32dp,
            context.getString(R.string.action_previous),
            retrievePlaybackAction(ACTION_REWIND)
        )
        val nextAction = NotificationCompat.Action(
            R.drawable.ic_skip_next_round_white_32dp,
            context.getString(R.string.action_next),
            retrievePlaybackAction(ACTION_SKIP)
        )
        val dismissAction = NotificationCompat.Action(
            R.drawable.ic_close,
            context.getString(R.string.action_cancel),
            retrievePlaybackAction(ACTION_QUIT)
        )
        setSmallIcon(R.drawable.ic_notification)
        setContentIntent(clickIntent)
        setDeleteIntent(deleteIntent)
        setShowWhen(false)
        addAction(toggleFavorite)
        addAction(previousAction)
        addAction(playPauseAction)
        addAction(nextAction)
        if (VersionUtils.hasS()) {
            addAction(dismissAction)
        }

        setStyle(
            MediaStyle()
                .setMediaSession(mediaSessionToken)
                .setShowActionsInCompactView(1, 2, 3)
        )
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    }

    override fun updateMetadata(song: Song, onUpdate: () -> Unit) {
        if (song == Song.emptySong) return
        setContentTitle(song.title)
        setContentText(song.artistName)
        setSubText(song.albumName)
        val bigNotificationImageSize = context.resources
            .getDimensionPixelSize(R.dimen.notification_big_image_size)
        GlideApp.with(context)
            .asBitmap()
            .songCoverOptions(song)
            .load(RetroGlideExtension.getSongModel(song))
            // .checkIgnoreMediaStore()
            .centerCrop()
            .into(object : CustomTarget<Bitmap>(
                bigNotificationImageSize,
                bigNotificationImageSize
            ) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        setLargeIcon(resource)
                        onUpdate()
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        setLargeIcon(
                            BitmapFactory.decodeResource(
                                context.resources,
                                R.drawable.default_audio_art
                            )
                        )
                        onUpdate()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        setLargeIcon(
                            BitmapFactory.decodeResource(
                                context.resources,
                                R.drawable.default_audio_art
                            )
                        )
                        onUpdate()
                    }
                })
    }

    private fun buildPlayAction(isPlaying: Boolean): NotificationCompat.Action {
        val playButtonResId =
            if (isPlaying) R.drawable.ic_pause_white_48dp else R.drawable.ic_play_arrow_white_48dp
        return NotificationCompat.Action.Builder(
            playButtonResId,
            context.getString(R.string.action_play_pause),
            retrievePlaybackAction(ACTION_TOGGLE_PAUSE)
        ).build()
    }

    private fun buildFavoriteAction(isFavorite: Boolean): NotificationCompat.Action {
        val favoriteResId =
            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        return NotificationCompat.Action.Builder(
            favoriteResId,
            context.getString(R.string.action_toggle_favorite),
            retrievePlaybackAction(TOGGLE_FAVORITE)
        ).build()
    }

    override fun setPlaying(isPlaying: Boolean) {
        mActions[2] = buildPlayAction(isPlaying)
    }

    override fun updateFavorite(isFavorite: Boolean) {
        mActions[0] = buildFavoriteAction(isFavorite)
    }

    private fun retrievePlaybackAction(action: String): PendingIntent {
        val serviceName = ComponentName(context, MusicService::class.java)
        val intent = Intent(action)
        intent.component = serviceName
        return PendingIntent.getService(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                if (VersionUtils.hasMarshmallow()) PendingIntent.FLAG_IMMUTABLE
                else 0
        )
    }

    companion object {

        fun from(
            context: MusicService,
            notificationManager: NotificationManager,
            mediaSession: MediaSessionCompat,
        ): PlayingNotification {
            if (VersionUtils.hasOreo()) {
                createNotificationChannel(context, notificationManager)
            }
            return PlayingNotificationImpl24(context, mediaSession.sessionToken)
        }
    }
}
