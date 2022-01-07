package com.mcgrady.xproject.module_samples.window_insets_controller

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.blankj.utilcode.util.ThreadUtils
import com.mcgrady.xproject.module_samples.R
import com.mcgrady.xproject.module_samples.databinding.ActivityWindowInsetsControllerBinding
import kotlin.concurrent.thread

class WindowInsetsControllerActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityWindowInsetsControllerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWindowInsetsControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        useWindowInsetsController()

        setSupportActionBar(binding.toolbar)

        val navController =
            findNavController(R.id.nav_host_fragment_content_window_insets_controller)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            findNavController(R.id.nav_host_fragment_content_window_insets_controller)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun useWindowInsetsController() {
        val controller = ViewCompat.getWindowInsetsController(binding.root)
        controller?.run {
            //隐藏状态栏
            hide(WindowInsetsCompat.Type.statusBars())
            //显示状态栏
            show(WindowInsetsCompat.Type.statusBars())
            //状态栏文字颜色改成黑色
            isAppearanceLightStatusBars = true

            //隐藏导航栏
            hide(WindowInsetsCompat.Type.navigationBars())
            //显示导航栏
            show(WindowInsetsCompat.Type.navigationBars())

            //显示键盘
            show(WindowInsetsCompat.Type.ime())
            ThreadUtils.runOnUiThreadDelayed({
                //隐藏键盘
                hide(WindowInsetsCompat.Type.ime())
            }, 2000)

            //隐藏所有系统栏
            hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}