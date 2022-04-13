package com.mcgrady.xproject.testing.samples.image

import android.os.Bundle
import com.mcgrady.xarchitecture.base.BaseActivity
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.testing.samples.databinding.ActivityImageViewScaleTypeSampleBinding

class ImageViewScaleTypeActivity : BaseActivity() {

    private val binding: ActivityImageViewScaleTypeSampleBinding by viewbind()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {

        }
    }
}