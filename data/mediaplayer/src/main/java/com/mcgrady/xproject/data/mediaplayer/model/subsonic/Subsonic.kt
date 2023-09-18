package com.mcgrady.xproject.data.mediaplayer.model.subsonic


import com.mcgrady.xproject.core.utils.PreferenceUtil
import com.mcgrady.xproject.data.mediaplayer.extensions.subsonicPassword
import com.mcgrady.xproject.data.mediaplayer.extensions.subsonicSalt
import com.mcgrady.xproject.data.mediaplayer.extensions.subsonicServer
import com.mcgrady.xproject.data.mediaplayer.extensions.subsonicToken
import com.mcgrady.xproject.data.mediaplayer.extensions.subsonicUser
import com.mcgrady.xproject.data.mediaplayer.utils.Constants

class Subsonic {
    val apiVersion: Version = API_MAX_VERSION

    /*var browsingClient: BrowsingClient? = null
        get() {
            if (field == null) {
                field = BrowsingClient(this)
            }
            return field
        }
        private set
    var mediaRetrievalClient: MediaRetrievalClient? = null
        get() {
            if (field == null) {
                field = MediaRetrievalClient(this)
            }
            return field
        }
        private set
    var playlistClient: PlaylistClient? = null
        get() {
            if (field == null) {
                field = PlaylistClient(this)
            }
            return field
        }
        private set
    var searchingClient: SearchingClient? = null
        get() {
            if (field == null) {
                field = SearchingClient(this)
            }
            return field
        }
        private set
    var albumSongListClient: AlbumSongListClient? = null
        get() {
            if (field == null) {
                field = AlbumSongListClient(this)
            }
            return field
        }
        private set
    var mediaAnnotationClient: MediaAnnotationClient? = null
        get() {
            if (field == null) {
                field = MediaAnnotationClient(this)
            }
            return field
        }
        private set
    var podcastClient: PodcastClient? = null
        get() {
            if (field == null) {
                field = PodcastClient(this)
            }
            return field
        }
        private set
    var mediaLibraryScanningClient: MediaLibraryScanningClient? = null
        get() {
            if (field == null) {
                field = MediaLibraryScanningClient(this)
            }
            return field
        }
        private set
    var bookmarksClient: BookmarksClient? = null
        get() {
            if (field == null) {
                field = BookmarksClient(this)
            }
            return field
        }
        private set
    var internetRadioClient: InternetRadioClient? = null
        get() {
            if (field == null) {
                field = InternetRadioClient(this)
            }
            return field
        }
        private set*/

    val url: String = "${PreferenceUtil.subsonicServer}/rest/".replace("//rest", "/rest")

    val params: Map<String, String> = mapOf(
        "u" to PreferenceUtil.subsonicUser,
        "p" to PreferenceUtil.subsonicPassword,
        "s" to PreferenceUtil.subsonicSalt,
        "t" to PreferenceUtil.subsonicToken,
        "v" to apiVersion.versionString,
        "c" to Constants.SUBSONIC_CLIENT_NAME,
        "f" to "json"
    )

    companion object {
        private val API_MAX_VERSION: Version = Version.of("1.15.0")
    }
}