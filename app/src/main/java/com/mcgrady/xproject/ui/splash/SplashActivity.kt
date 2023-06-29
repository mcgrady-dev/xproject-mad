package com.mcgrady.xproject.ui.splash

import android.content.Intent
import android.os.Bundle
import com.mcgrady.xarch.extension.viewBinding
import com.mcgrady.xproject.core.base.BaseActivity
import com.mcgrady.xproject.ui.main.MainActivity
import com.mcgrady.xproject.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {

    private val binding by viewBinding(ActivitySplashBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
    }

    override fun onBackPressed() {
        //onBackPressedDispatcher.onBackPressed()
    }
}