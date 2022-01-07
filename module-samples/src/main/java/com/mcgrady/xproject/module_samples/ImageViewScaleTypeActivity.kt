package com.mcgrady.xproject.module_samples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mcgrady.xarchitecture.base.BaseActivity
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.module_samples.databinding.ActivityImageViewScaleTypeSampleBinding

class ImageViewScaleTypeActivity : BaseActivity() {

    private val binding: ActivityImageViewScaleTypeSampleBinding by viewbind()

    override fun initView(savedInstanceState: Bundle?) {
        with(binding) {

        }
    }

    override fun initData() {

    }
}