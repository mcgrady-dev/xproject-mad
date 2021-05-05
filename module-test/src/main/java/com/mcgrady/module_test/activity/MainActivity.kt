package com.mcgrady.module_test.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.savedstate.SavedStateRegistry
import com.mcgrady.module_test.R
import com.mcgrady.module_test.fragment.MyFragment
import com.mcgrady.module_test.fragment.MyFragmentFactory
import com.mcgrady.module_test.lifecycle.FragmentLifecycle

/**
 * Created by mcgrady on 1/29/21.
 */
class MainActivity: AppCompatActivity(R.layout.activity_main) {

    companion object {
        private const val MY_SAVED_STATE_KEY = "my_saved_state"
        private const val SOME_VALUE_KEY = "some_value"
    }

    private lateinit var someValue: String

    private val savedStateProvider = SavedStateRegistry.SavedStateProvider {
        Bundle().apply {
            putString(SOME_VALUE_KEY, someValue)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(javaClass.simpleName)

        someMethod()
        savedStateRegistry
            .registerSavedStateProvider(MY_SAVED_STATE_KEY, savedStateProvider)
    }

    fun someMethod() {
        someValue = savedStateRegistry
            .consumeRestoredStateForKey(MY_SAVED_STATE_KEY)
            ?.getString(SOME_VALUE_KEY)
            ?:""
    }
}