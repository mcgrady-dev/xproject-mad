package com.mcgrady.xproject

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.SpanUtils
import com.google.android.material.snackbar.Snackbar
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.common.core.log.Log
import com.mcgrady.xproject.databinding.ActivityMainBinding
import com.tencent.vasdolly.helper.ChannelReaderUtil
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private val binding: ActivityMainBinding by viewbind()

    @VisibleForTesting
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            tvTitle.text = "HELLO WORLD"
            tvTitle.setTextColor(Color.BLACK)
            tvTitle.setOnClickListener {
                val dm = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(dm)
                val width = Math.min(dm.widthPixels, dm.heightPixels)

                Log.d(TAG,"MainActivity: dpi : ${dm.densityDpi} smallest width pixels : $width")


                Log.d(TAG,"MainActivity: 计算出来的smallestWidth : " + width / (dm.densityDpi / 160.0) + "dp")
//                LogUtils.d("实际使用的smallestWidth :  " + resources.getString(R.string.base_dpi))
            }

            tvChannel.text = ChannelReaderUtil.getChannel(applicationContext) ?: "null"
        }

        viewModel.currentName.observe(this) {
            binding.tvTitle.text = it
        }

        viewModel.currentName.value = "XXXXX"

//        viewModel.taskUpdate.observe(this, {
//            Snackbar.make(binding.root, "HELLO WORLD", Snackbar.LENGTH_LONG).show()
//        })

//        viewModel.taskUpdate.call()

        NetworkLiveData.getInstance(this).observe(this) {
            when (it) {
                NetworkState.UNAVAILABLE -> {
                    Snackbar.make(binding.root, "网络不可用", Snackbar.LENGTH_SHORT).show()
                }
                NetworkState.NONE -> {
                    Snackbar.make(binding.root, "网络断开", Snackbar.LENGTH_SHORT).show()
                }
                NetworkState.CONNECT -> {
                    Snackbar.make(binding.root, "网络已连接", Snackbar.LENGTH_SHORT).show()
                }
                NetworkState.WIFI -> {
                    Snackbar.make(binding.root, "WIFI已连接", Snackbar.LENGTH_SHORT).show()
                }
                NetworkState.CELLULAR -> {
                    Snackbar.make(binding.root, "移动网络已连接", Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }

//        testShareMedia()
    }

    private fun testShareMedia() {
        //获取目录：/storage/emulated/0/
        val rootFile: File = Environment.getExternalStorageDirectory()
        val imagePath: String =
            rootFile.absolutePath + File.separator + Environment.DIRECTORY_PICTURES + File.separator.toString() + "myPic.png"
        val bitmap = BitmapFactory.decodeFile(imagePath)

        Log.d(TAG, bitmap?.toString() ?: "not found")

        val legacy = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Environment.isExternalStorageLegacy()
        Log.d(TAG, "isExternalStorageLegacy = $legacy")

        SpanUtils.with(binding.tvTitle)
            .appendLine("getRootPath " + PathUtils.getRootPath())
            .appendLine("getDataPath " + PathUtils.getDataPath())
            .appendLine("getDownloadCachePath " + PathUtils.getDownloadCachePath())
            .appendLine("getInternalAppDataPath " + PathUtils.getInternalAppDataPath())
            .appendLine("getInternalAppCodeCacheDir " + PathUtils.getInternalAppCodeCacheDir())
            .appendLine("getInternalAppCachePath " + PathUtils.getInternalAppCachePath())
            .appendLine("getInternalAppDbsPath " + PathUtils.getInternalAppDbsPath())
            .appendLine("getInternalAppDbPath " + PathUtils.getInternalAppDbPath("demo"))
            .appendLine("getInternalAppFilesPath " + PathUtils.getInternalAppFilesPath())
            .appendLine("getInternalAppSpPath " + PathUtils.getInternalAppSpPath())
            .appendLine("getInternalAppNoBackupFilesPath " + PathUtils.getInternalAppNoBackupFilesPath())
            .appendLine()
            .appendLine("getExternalStoragePath " + PathUtils.getExternalStoragePath())
            .appendLine("getExternalMusicPath " + PathUtils.getExternalMusicPath())
            .appendLine("getExternalPodcastsPath " + PathUtils.getExternalPodcastsPath())
            .appendLine("getExternalRingtonesPath " + PathUtils.getExternalRingtonesPath())
            .appendLine("getExternalAlarmsPath " + PathUtils.getExternalAlarmsPath())
            .appendLine("getExternalNotificationsPath " + PathUtils.getExternalNotificationsPath())
            .appendLine("getExternalPicturesPath " + PathUtils.getExternalPicturesPath())
            .appendLine("getExternalMoviesPath " + PathUtils.getExternalMoviesPath())
            .appendLine("getExternalDownloadsPath " + PathUtils.getExternalDownloadsPath())
            .appendLine("getExternalDcimPath " + PathUtils.getExternalDcimPath())
            .appendLine("getExternalDocumentsPath " + PathUtils.getExternalDocumentsPath())
            .appendLine()
            .appendLine("getExternalAppDataPath " + PathUtils.getExternalAppDataPath())
            .appendLine("getExternalAppCachePath " + PathUtils.getExternalAppCachePath())
            .appendLine("getExternalAppFilesPath " + PathUtils.getExternalAppFilesPath())
            .appendLine("getExternalAppMusicPath " + PathUtils.getExternalAppMusicPath())
            .appendLine("getExternalAppPodcastsPath " + PathUtils.getExternalAppPodcastsPath())
            .appendLine("getExternalAppRingtonesPath " + PathUtils.getExternalAppRingtonesPath())
            .appendLine("getExternalAppAlarmsPath " + PathUtils.getExternalAppAlarmsPath())
            .appendLine("getExternalAppNotificationsPath " + PathUtils.getExternalAppNotificationsPath())
            .appendLine("getExternalAppPicturesPath " + PathUtils.getExternalAppPicturesPath())
            .appendLine("getExternalAppMoviesPath " + PathUtils.getExternalAppMoviesPath())
            .appendLine("getExternalAppDownloadPath " + PathUtils.getExternalAppDownloadPath())
            .appendLine("getExternalAppDcimPath " + PathUtils.getExternalAppDcimPath())
            .appendLine("getExternalAppDocumentsPath " + PathUtils.getExternalAppDocumentsPath())
            .appendLine("getExternalAppObbPath " + PathUtils.getExternalAppObbPath())
            .create()

        Log.d(TAG, binding.tvTitle.text.toString())
    }

}