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
package com.mcgrady.xproject.retromusic.activities.tageditor

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.mcgrady.xproject.retromusic.extensions.showToast
import com.mcgrady.xproject.retromusic.misc.UpdateToastMediaScannerCompletionListener
import com.mcgrady.xproject.retromusic.model.AudioTagInfo
import com.mcgrady.xproject.retromusic.util.MusicUtil.createAlbumArtFile
import com.mcgrady.xproject.retromusic.util.MusicUtil.deleteAlbumArt
import com.mcgrady.xproject.retromusic.util.MusicUtil.insertAlbumArt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.exceptions.CannotReadException
import org.jaudiotagger.audio.exceptions.CannotWriteException
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException
import org.jaudiotagger.tag.TagException
import org.jaudiotagger.tag.images.AndroidArtwork
import org.jaudiotagger.tag.images.Artwork
import java.io.File
import java.io.IOException

class TagWriter {

    companion object {

        suspend fun scan(context: Context, toBeScanned: List<String?>?) {
            if (toBeScanned == null || toBeScanned.isEmpty()) {
                Log.i("scan", "scan: Empty")
                context.showToast("Scan file from folder")
                return
            }
            MediaScannerConnection.scanFile(
                context,
                toBeScanned.toTypedArray(),
                null,
                withContext(Dispatchers.Main) {
                    if (context is Activity) UpdateToastMediaScannerCompletionListener(
                        context, toBeScanned
                    ) else null
                }
            )
        }

        suspend fun writeTagsToFiles(context: Context, info: AudioTagInfo) {
            withContext(Dispatchers.IO) {
                runCatching {
                    var artwork: Artwork? = null
                    var albumArtFile: File? = null
                    if (info.artworkInfo?.artwork != null) {
                        try {
                            albumArtFile = createAlbumArtFile(context).canonicalFile
                            info.artworkInfo.artwork.compress(
                                Bitmap.CompressFormat.JPEG,
                                100,
                                albumArtFile.outputStream()
                            )
                            artwork = AndroidArtwork.createArtworkFromFile(albumArtFile)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    var wroteArtwork = false
                    var deletedArtwork = false
                    for (filePath in info.filePaths!!) {
                        try {
                            val audioFile = AudioFileIO.read(File(filePath))
                            val tag = audioFile.tagOrCreateAndSetDefault
                            if (info.fieldKeyValueMap != null) {
                                for ((key, value) in info.fieldKeyValueMap) {
                                    try {
                                        tag.setField(key, value)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                            if (info.artworkInfo != null) {
                                if (info.artworkInfo.artwork == null) {
                                    tag.deleteArtworkField()
                                    deletedArtwork = true
                                } else if (artwork != null) {
                                    tag.deleteArtworkField()
                                    tag.setField(artwork)
                                    wroteArtwork = true
                                }
                            }
                            audioFile.commit()
                        } catch (e: CannotReadException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: CannotWriteException) {
                            e.printStackTrace()
                        } catch (e: TagException) {
                            e.printStackTrace()
                        } catch (e: ReadOnlyFileException) {
                            e.printStackTrace()
                        } catch (e: InvalidAudioFrameException) {
                            e.printStackTrace()
                        }
                    }
                    if (wroteArtwork) {
                        insertAlbumArt(context, info.artworkInfo!!.albumId, albumArtFile!!.path)
                    } else if (deletedArtwork) {
                        deleteAlbumArt(context, info.artworkInfo!!.albumId)
                    }
                    scan(context, info.filePaths)
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.R)
        suspend fun writeTagsToFilesR(context: Context, info: AudioTagInfo): List<File> =
            withContext(Dispatchers.IO) {
                val cacheFiles = mutableListOf<File>()
                runCatching {
                    var artwork: Artwork? = null
                    var albumArtFile: File? = null
                    if (info.artworkInfo?.artwork != null) {
                        try {
                            albumArtFile = createAlbumArtFile(context).canonicalFile
                            info.artworkInfo.artwork.compress(
                                Bitmap.CompressFormat.JPEG,
                                100,
                                albumArtFile.outputStream()
                            )
                            artwork = AndroidArtwork.createArtworkFromFile(albumArtFile)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    var wroteArtwork = false
                    var deletedArtwork = false
                    for (filePath in info.filePaths!!) {
                        try {
                            val originFile = File(filePath)
                            val cacheFile = File(context.cacheDir, originFile.name)
                            cacheFiles.add(cacheFile)
                            originFile.inputStream().use { input ->
                                cacheFile.outputStream().use { output ->
                                    input.copyTo(output)
                                }
                            }
                            val audioFile = AudioFileIO.read(cacheFile)
                            val tag = audioFile.tagOrCreateAndSetDefault
                            if (info.fieldKeyValueMap != null) {
                                for ((key, value) in info.fieldKeyValueMap) {
                                    try {
                                        tag.setField(key, value)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                            if (info.artworkInfo != null) {
                                if (info.artworkInfo.artwork == null) {
                                    tag.deleteArtworkField()
                                    deletedArtwork = true
                                } else if (artwork != null) {
                                    tag.deleteArtworkField()
                                    tag.setField(artwork)
                                    wroteArtwork = true
                                }
                            }
                            audioFile.commit()
                        } catch (e: CannotReadException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: CannotWriteException) {
                            e.printStackTrace()
                        } catch (e: TagException) {
                            e.printStackTrace()
                        } catch (e: ReadOnlyFileException) {
                            e.printStackTrace()
                        } catch (e: InvalidAudioFrameException) {
                            e.printStackTrace()
                        }
                    }
                    if (wroteArtwork) {
                        insertAlbumArt(context, info.artworkInfo!!.albumId, albumArtFile!!.path)
                    } else if (deletedArtwork) {
                        deleteAlbumArt(context, info.artworkInfo!!.albumId)
                    }
                }.onFailure {
                    it.printStackTrace()
                }
                cacheFiles
            }
    }
}
