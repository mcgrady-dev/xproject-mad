package com.mcgrady.xproject.testing.samples.bundle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.mcgrady.xproject.testing.samples.R
import com.mcgrady.xproject.testing.samples.image.ShapeActivity

class BundleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bundle)
    }

    fun startOtherActivity() {
        val bundle = bundleOf(
            "KEY_INT" to 1,
            "KEY_LONG" to 1L,
            "KEY_BOOLEAN" to true,
            "KEY_NULL" to null,
            "KEY_ARRAY" to arrayOf(1.0F, 2.0F, 3.0F)
        )

        startActivity(Intent(this, ShapeActivity::class.java), bundle)
    }
}