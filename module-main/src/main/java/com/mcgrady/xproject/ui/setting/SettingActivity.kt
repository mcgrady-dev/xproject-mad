package com.mcgrady.xproject.ui.setting

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.mcgrady.xarchitecture.base.BaseActivity
import com.mcgrady.xproject.common.core.log.Log
import com.mcgrady.xproject.ui.main.R

class SettingActivity : BaseActivity(), NavController.OnDestinationChangedListener {

//    private val binding: ActivitySettingBinding by viewbind()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main_setting) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        Log.d("destination=$destination")
    }

    override fun onDestroy() {
        super.onDestroy()
        findNavController(R.id.nav_host_fragment_main_setting)?.removeOnDestinationChangedListener(
            this
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}