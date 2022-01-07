package com.mcgrady.xproject.testing.samples

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.mcgrady.xarchitecture.base.BaseActivity
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.testing.samples.databinding.ActivityShapeSampleBinding

class ShapeActivity : BaseActivity() {

    private val binding: ActivityShapeSampleBinding by viewbind()

    override fun initView(savedInstanceState: Bundle?) {
        
        binding.btnChangeShapeSoild.setOnClickListener {
            //动态修改Shape的solid属性的color值
            val drawable = binding.viewShape.background as GradientDrawable
            drawable.color = ContextCompat.getColorStateList(this,
                R.color.design_default_color_error
            )
        }
    }

    override fun initData() {

    }

}