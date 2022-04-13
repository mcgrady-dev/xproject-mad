package com.mcgrady.xproject.testing.samples.image

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.mcgrady.xarchitecture.base.BaseActivity
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.testing.samples.R
import com.mcgrady.xproject.testing.samples.databinding.ActivityShapeSampleBinding

class ShapeActivity : BaseActivity() {

    private val binding: ActivityShapeSampleBinding by viewbind()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            btnChangeShapeSoild.setOnClickListener {
                //动态修改Shape的solid属性的color值
                val drawable = binding.viewShape.background as GradientDrawable
                drawable.color = ContextCompat.getColorStateList(this@ShapeActivity,
                    R.color.design_default_color_error
                )
            }
        }
    }
}