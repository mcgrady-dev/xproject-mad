package com.mcgrady.xproject.testing.test

import android.os.Bundle
import com.mcgrady.xarchitecture.base.BaseActivity
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.testing.test.databinding.ActivityCardViewBinding

class CardViewActivity : BaseActivity() {

    private val binding: ActivityCardViewBinding by viewbind()

    override fun initView(savedInstanceState: Bundle?) {
        with(binding) {

        }
    }

    override fun initData() {

    }


}