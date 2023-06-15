package com.mcgrady.xproject.common.widget.transformationlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mcgrady.xproject.common.widget.transformationlayout.TransformationCompat.activityTransitionName

/** An abstract activity extending [AppCompatActivity] with registering transformation automatically. */
abstract class TransformationAppCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainer(intent.getParcelableExtra(activityTransitionName))
        super.onCreate(savedInstanceState)
    }
}