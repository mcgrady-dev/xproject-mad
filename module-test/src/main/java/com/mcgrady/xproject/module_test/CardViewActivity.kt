package com.mcgrady.xproject.module_test

import android.os.Bundle
import com.mcgrady.xarchitecture.base.BaseActivity
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.module_test.databinding.ActivityCardViewBinding

class CardViewActivity : BaseActivity() {

    private val binding: ActivityCardViewBinding by viewbind()

    override fun initView(savedInstanceState: Bundle?) {
        with(binding) {

        }
    }

    override fun initData() {

    }


}