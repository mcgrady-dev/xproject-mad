package com.mcgrady.xproject

import com.mcgrady.xproject.common.core.base.BaseActivity
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.databinding.ActivityTestBinding

/**
 * Created by mcgrady on 2021/7/18.
 */
class TestViewBindingActivity : BaseActivity() {

    val binding: ActivityTestBinding by viewbind()
}