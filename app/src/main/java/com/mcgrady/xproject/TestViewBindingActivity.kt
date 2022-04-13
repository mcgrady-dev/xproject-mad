package com.mcgrady.xproject

import android.os.Bundle
import com.mcgrady.xarchitecture.base.BaseActivity
import com.mcgrady.xarchitecture.ext.viewbind
import com.mcgrady.xproject.databinding.ActivityTestBinding

/**
 * Created by mcgrady on 2021/7/18.
 */
class TestViewBindingActivity : BaseActivity() {

    val binding: ActivityTestBinding by viewbind()
}