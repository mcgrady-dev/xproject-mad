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
package com.mcgrady.xproject.retromusic.fragments.backup

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcgrady.xproject.retromusic.activities.MainActivity
import com.mcgrady.xproject.retromusic.helper.BackupContent
import com.mcgrady.xproject.retromusic.helper.BackupHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import kotlin.system.exitProcess

class BackupViewModel : ViewModel() {
    private val backupsMutableLiveData = MutableLiveData<List<File>>()
    val backupsLiveData: LiveData<List<File>> = backupsMutableLiveData

    fun loadBackups() {
        BackupHelper.getBackupRoot().listFiles { _, name ->
            return@listFiles name.endsWith(BackupHelper.BACKUP_EXTENSION)
        }?.toList()?.let {
            backupsMutableLiveData.value = it
        }
    }

    suspend fun restoreBackup(activity: Activity, inputStream: InputStream?, contents: List<BackupContent>) {
        BackupHelper.restoreBackup(activity, inputStream, contents)
        if (contents.contains(BackupContent.SETTINGS) or contents.contains(BackupContent.CUSTOM_ARTIST_IMAGES)) {
            // We have to restart App when Preferences i.e. Settings or Artist Images are to be restored
            withContext(Dispatchers.Main) {
                val intent = Intent(
                    activity,
                    MainActivity::class.java
                )
                activity.startActivity(intent)
                exitProcess(0)
            }
        }
    }
}
