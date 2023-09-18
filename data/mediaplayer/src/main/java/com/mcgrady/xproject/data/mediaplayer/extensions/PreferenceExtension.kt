package com.mcgrady.xproject.data.mediaplayer.extensions

import com.mcgrady.xproject.core.utils.PreferenceUtil


const val THEME = "theme"
private const val SERVER = "server"
private const val USER = "user"
private const val PASSWORD = "password"
private const val TOKEN = "token"
private const val SALT = "salt"
private const val LOW_SECURITY = "low_security"
private const val BATTERY_OPTIMIZATION = "battery_optimization"
private const val SERVER_ID = "server_id"
private const val PLAYBACK_SPEED = "playback_speed"
private const val SKIP_SILENCE = "skip_silence"
private const val IMAGE_CACHE_SIZE = "image_cache_size"
private const val IMAGE_SIZE = "image_size"
private const val MAX_BITRATE_WIFI = "max_bitrate_wifi"
private const val MAX_BITRATE_MOBILE = "max_bitrate_mobile"
private const val AUDIO_TRANSCODE_FORMAT_WIFI = "audio_transcode_format_wifi"
private const val AUDIO_TRANSCODE_FORMAT_MOBILE = "audio_transcode_format_mobile"
private const val WIFI_ONLY = "wifi_only"
private const val DATA_SAVING_MODE = "data_saving_mode"
private const val SERVER_UNREACHABLE = "server_unreachable"
private const val SYNC_STARRED_TRACKS_FOR_OFFLINE_USE = "sync_starred_tracks_for_offline_use"
private const val QUEUE_SYNCING = "queue_syncing"
private const val QUEUE_SYNCING_COUNTDOWN = "queue_syncing_countdown"
private const val ROUNDED_CORNER = "rounded_corner"
private const val ROUNDED_CORNER_SIZE = "rounded_corner_size"
private const val PODCAST_SECTION_VISIBILITY = "podcast_section_visibility"
private const val RADIO_SECTION_VISIBILITY = "radio_section_visibility"
private const val MUSIC_DIRECTORY_SECTION_VISIBILITY = "music_directory_section_visibility"
private const val REPLAY_GAIN_MODE = "replay_gain_mode"
private const val AUDIO_TRANSCODE_PRIORITY = "audio_transcode_priority"
private const val DOWNLOAD_STORAGE = "download_storage"
private const val DEFAULT_DOWNLOAD_VIEW_TYPE = "default_download_view_type"
private const val AUDIO_TRANSCODE_DOWNLOAD = "audio_transcode_download"
private const val AUDIO_TRANSCODE_DOWNLOAD_PRIORITY = "audio_transcode_download_priority"
private const val MAX_BITRATE_DOWNLOAD = "max_bitrate_download"
private const val AUDIO_TRANSCODE_FORMAT_DOWNLOAD = "audio_transcode_format_download"

val PreferenceUtil.subsonicServer: String
    get() = decodeString(SERVER)

val PreferenceUtil.subsonicServerId: String
    get() = decodeString(SERVER_ID)
val PreferenceUtil.subsonicUser: String
    get() = decodeString(USER)
val PreferenceUtil.subsonicPassword: String
    get() = decodeString(PASSWORD)
val PreferenceUtil.subsonicToken: String
    get() = decodeString(TOKEN)

val PreferenceUtil.subsonicSalt: String
    get() = decodeString(SALT)
